package com.swehd.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    @Test
    void isValidReturnTrue() {
        String email = "user@email.com";
        boolean actualResult = UserService.isValid(email);
        assertTrue(actualResult);
    }

    @Test
    void isValidReturnFalse() {
        String email = "useremail.com";
        boolean actualResult = UserService.isValid(email);
        assertFalse(actualResult);
    }

    @Test
    void nameLengthGood() {
        String name = "username";
        boolean result = UserService.shortThanThree(name);
        assertTrue(result);
    }

    @Test
    void nameLengthBad() {
        String name = "u";
        boolean result = UserService.shortThanThree(name);
        assertFalse(result);
    }

    @Test
    void nameAndEmailExists() {
        User user = new User();
        user.setName("mouse");
        user.setEmail("mouse@email.com");




    }
}