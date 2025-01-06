package com.bibek.service.user.impl;

import com.bibek.config.JwtProvider;
import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.User;
import com.bibek.repository.UserRepository;
import com.bibek.response.UserResponse;
import com.bibek.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final CustomMessageSource customMessageSource;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, CustomMessageSource customMessageSource) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public UserResponse findUserByJwtToken(String jwtToken) {
        String email = jwtProvider.getEmailFromToken(jwtToken);

        User user = this.findUserByEmail(email);

        if(user == null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.USER, customMessageSource.get(MessageConstants.CRUD_NOT_EXIST)));
        }

        return new UserResponse(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
