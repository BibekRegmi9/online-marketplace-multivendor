package com.bibek.controller.auth;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.USER_ROLE;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.User;
import com.bibek.model.VerificationCode;
import com.bibek.repository.UserRepository;
import com.bibek.request.LoginRequest;
import com.bibek.request.SignupRequest;
import com.bibek.response.AuthResponse;
import com.bibek.service.auth.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final CustomMessageSource customMessageSource;

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(CustomMessageSource customMessageSource, UserRepository userRepository, AuthService authService) {
        this.customMessageSource = customMessageSource;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<GlobalApiResponse> createUserHandler(@RequestBody SignupRequest signupRequest){
        String jwt = authService.registerUser(signupRequest);
//        AuthResponse response = new AuthResponse();
//        response.setJwt(jwt);
//        response.setMessage(MessageConstants.USER_REGISTRATION);
//        response.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.USER_REGISTRATION), jwt));
    }

    @PostMapping("/send/signup-login-otp")
    public ResponseEntity<GlobalApiResponse> sendOtp(@RequestBody VerificationCode email){
        try {
            authService.sendLoginOtp(email.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.SEND_OTP), null));
    }

    @PostMapping("/signin")
    public ResponseEntity<GlobalApiResponse> signIn(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.LOGIN_SUCCESS), authService.login(loginRequest)));
    }

}
