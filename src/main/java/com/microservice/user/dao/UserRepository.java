package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
