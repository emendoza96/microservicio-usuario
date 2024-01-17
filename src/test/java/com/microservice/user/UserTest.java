package com.microservice.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.microservice.user.domain.User;

public class UserTest {

    @Test
    public void testConstructorUsuario() {
        // Arrange
        String expected_username = "John837";
        String expected_password = "12345678";

        // Act
        User usertest = new User(expected_username, expected_password);
        
        // Assert
        assertEquals(expected_username, usertest.getUsername());
        assertEquals(expected_password, usertest.getPassword());
    }
}