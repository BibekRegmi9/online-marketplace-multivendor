package com.bibek.service.auth.impl;

import com.bibek.config.JwtProvider;
import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.USER_ROLE;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Cart;
import com.bibek.model.User;
import com.bibek.model.VerificationCode;
import com.bibek.repository.CartRepository;
import com.bibek.repository.UserRepository;
import com.bibek.repository.VerificationCodeRepository;
import com.bibek.request.LoginRequest;
import com.bibek.request.SignupRequest;
import com.bibek.response.AuthResponse;
import com.bibek.service.auth.AuthService;
import com.bibek.service.email.EmailService;
import com.bibek.utils.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomMessageSource customMessageSource;
    private final EmailService emailService;

    private final CustomUserService customUserService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository, JwtProvider jwtProvider, VerificationCodeRepository verificationCodeRepository, CustomMessageSource customMessageSource, EmailService emailService, CustomUserService customUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtProvider = jwtProvider;
        this.verificationCodeRepository = verificationCodeRepository;
        this.customMessageSource = customMessageSource;
        this.emailService = emailService;
        this.customUserService = customUserService;
    }

    @Override
    @Transactional
    public String registerUser(SignupRequest request) {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.WRONG_OTP));
        }

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null){
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getFullName());
            newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            newUser.setMobile("9567890876");
            newUser.setPassword(passwordEncoder.encode(request.getOtp()));

            user = userRepository.save(newUser);

            //cart
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateJwtToken(authentication);

        return jwtBuilder(request.getEmail());

    }

    @Override
    public void sendLoginOtp(String email) throws MessagingException {
        String SIGNING_PREFIX="signing_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);
            if(user == null){
                throw new CustomRunTimeException(customMessageSource.get(MessageConstants.ERROR_DOESNT_EXIST, customMessageSource.get(MessageConstants.USER)));
            }

        }

        VerificationCode oldVerificationCode = verificationCodeRepository.findByEmail(email);
        if(oldVerificationCode != null){
            verificationCodeRepository.delete(oldVerificationCode);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode newVerificationCode = new VerificationCode();
        newVerificationCode.setOtp(otp);
        newVerificationCode.setEmail(email);
        verificationCodeRepository.save(newVerificationCode);

        String subject = "Mero Pasal login/signup otp";
        String text = "Your login/signup otp is: " + otp;

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String otp = loginRequest.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

//        AuthResponse
        return jwtProvider.generateJwtToken(authentication);
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid username");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Invalid OTP");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    String jwtBuilder(String email){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateJwtToken(authentication);
    }
}
