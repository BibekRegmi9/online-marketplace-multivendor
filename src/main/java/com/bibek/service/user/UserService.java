package com.bibek.service.user;

import com.bibek.model.User;
import com.bibek.response.UserResponse;

public interface UserService {
    UserResponse findUserByJwtToken(String jwtToken);

    User findUserByEmail(String email);
}
