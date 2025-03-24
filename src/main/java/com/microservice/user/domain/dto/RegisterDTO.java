package com.microservice.user.domain.dto;

import com.microservice.user.domain.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private Role.UserRole role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role.UserRole getRole() {
        return role;
    }

    public void setRole(Role.UserRole role) {
        this.role = role;
    }


}
