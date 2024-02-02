package com.microservice.user.service;

import java.util.List;
import java.util.Optional;


import com.microservice.user.domain.Employee;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee);
    public Optional<Employee> getEmployeeById(Integer id);
    public Optional<Employee> getEmployeeByEmail(String email);
    public List<Employee> getAllEmployees();
    public void deleteEmployee(Integer id);
    public Boolean validateEmployee(Employee employee);
}
