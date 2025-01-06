package com.bibek.controller.user;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final CustomMessageSource customMessageSource;
    private final UserService userService;

    public UserController(CustomMessageSource customMessageSource, UserService userService) {
        this.customMessageSource = customMessageSource;
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ResponseEntity<GlobalApiResponse> getUserDetails(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.USER, customMessageSource.get(MessageConstants.CRUD_GET)), userService.findUserByJwtToken(jwt)));
    }
}
