package com.microservice.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    public Optional<Employee> findByEmail(String email);

}
