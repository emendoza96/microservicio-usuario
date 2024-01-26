package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
