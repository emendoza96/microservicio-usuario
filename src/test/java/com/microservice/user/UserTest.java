package com.microservice.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.microservice.user.domain.UserEntity;

public class UserTest {

    @Test
    public void testConstructorUsuario() {
        // Arrange
        String expected_username = "John837";
        String expected_password = "12345678";

        // Act
        UserEntity usertest = new UserEntity(expected_username, expected_password);

        // Assert
        assertEquals(expected_username, usertest.getUsername());
        assertEquals(expected_password, usertest.getPassword());
    }
}