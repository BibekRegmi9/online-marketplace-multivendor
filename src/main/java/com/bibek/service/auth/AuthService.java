package com.bibek.service.auth;


import com.bibek.enums.USER_ROLE;
import com.bibek.request.LoginRequest;
import com.bibek.request.SignupRequest;
import com.bibek.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    String registerUser(SignupRequest request);

    void sendLoginOtp(String email, USER_ROLE role) throws MessagingException;

    String login(LoginRequest loginRequest);
}
