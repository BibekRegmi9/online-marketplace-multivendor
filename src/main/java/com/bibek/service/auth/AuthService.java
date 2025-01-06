package com.bibek.service.auth;


import com.bibek.request.LoginRequest;
import com.bibek.request.SignupRequest;
import com.bibek.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    String registerUser(SignupRequest request);

    void sendLoginOtp(String email) throws MessagingException;

    String login(LoginRequest loginRequest);
}
