package com.bibek.user;

import com.bibek.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAdd(){
        assertEquals(10, 5+5);
    }

//    @Disabled
    @Test
    public void testFindUserByEmail(){
        assertEquals(4, userRepository.findByEmail("bibek@gmail.com"));
        assertNotNull(userRepository.findByEmail("bibek@gmail.com"));
        assertTrue(5 > 1);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 10, 12",
            "3, 3, 9"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }
}
