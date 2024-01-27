package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
