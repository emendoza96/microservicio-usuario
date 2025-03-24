package com.microservice.user.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Role;
import com.microservice.user.domain.Role.UserRole;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByType(UserRole role);
}
