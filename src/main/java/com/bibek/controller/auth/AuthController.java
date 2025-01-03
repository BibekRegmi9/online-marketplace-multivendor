package com.bibek.controller.auth;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.User;
import com.bibek.repository.UserRepository;
import com.bibek.request.SignupRequest;
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

    public AuthController(CustomMessageSource customMessageSource, UserRepository userRepository) {
        this.customMessageSource = customMessageSource;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<GlobalApiResponse> createUserHandler(@RequestBody SignupRequest signupRequest){
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.USER_REGISTRATION), null));
    }

}
