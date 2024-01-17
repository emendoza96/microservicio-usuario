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
        String expected_email = "john@gmail.com";
        String expected_name = "John";
        String expected_lastname = "Doe";

        // Act
        User usertest = new User(
            expected_username, 
            expected_password, 
            expected_email, 
            expected_name, 
            expected_lastname
        );
        
        // Assert
        assertEquals(expected_username, usertest.getUsername());
        assertEquals(expected_password, usertest.getPassword());
        assertEquals(expected_email, usertest.getEmail());
        assertEquals(expected_name, usertest.getName());
        assertEquals(expected_lastname, usertest.getLastname());
    }
}