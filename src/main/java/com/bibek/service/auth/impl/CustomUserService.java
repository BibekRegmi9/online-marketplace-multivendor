package com.bibek.service.auth.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.USER_ROLE;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Seller;
import com.bibek.model.User;
import com.bibek.repository.SellerRepository;
import com.bibek.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final String SELLER_PREFIX = "seller_";

    private final CustomMessageSource customMessageSource;

    private final SellerRepository sellerRepository;

    public CustomUserService(UserRepository userRepository, CustomMessageSource customMessageSource, SellerRepository sellerRepository) {
        this.userRepository = userRepository;
        this.customMessageSource = customMessageSource;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
            String actualUserName = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUserName);

            if(seller != null){
                return buildUserDetail(seller.getEmail(), seller.getPassword(), seller.getRole());
            }
        } else {
            User user = userRepository.findByEmail(username);

            if(user != null){
                return buildUserDetail(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, MessageConstants.USER));
    }

    private UserDetails buildUserDetail(String email, String password, USER_ROLE role) {
        if(role == null){
            role = USER_ROLE.ROLE_CUSTOMER;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}
