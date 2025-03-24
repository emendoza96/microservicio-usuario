package com.microservice.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.microservice.user.dao.jpa.RoleRepository;
import com.microservice.user.dao.jpa.UserRepository;
import com.microservice.user.domain.Role;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.domain.dto.RegisterDTO;
import com.microservice.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(RegisterDTO user) {

        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByType(user.getRole()).orElseThrow(() -> new IllegalArgumentException("User role not found"));
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public UserEntity getAuthenticatedUser() {
        return null;
    }

}
