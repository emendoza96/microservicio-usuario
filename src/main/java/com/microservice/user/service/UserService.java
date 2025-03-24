package com.microservice.user.service;

import com.microservice.user.domain.UserEntity;
import com.microservice.user.domain.dto.RegisterDTO;

public interface UserService {

    UserEntity registerUser(RegisterDTO user);
    UserEntity getAuthenticatedUser();

}