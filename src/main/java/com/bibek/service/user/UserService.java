package com.bibek.service.user;

import com.bibek.model.User;
import com.bibek.response.UserResponse;

public interface UserService {
    User findUserByJwtToken(String jwtToken);

    User findUserByEmail(String email);
}
