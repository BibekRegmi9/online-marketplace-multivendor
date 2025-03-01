package com.bibek.user;

import com.bibek.enums.USER_ROLE;
import com.bibek.model.User;
import com.bibek.repository.UserRepository;
import com.bibek.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


public class UserServiceImplTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findUserByEmailTest(){
        when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(
                User.builder().id(1L)
                        .email("bibek@gmail.com")
                        .password("password")
                        .role(USER_ROLE.ROLE_SELLER)
                        .fullName("Bibek Regmi").build()
        );
        User user = userService.findUserByEmail("bibek@gmail.com");
        Assertions.assertNotNull(user);

    }
}
