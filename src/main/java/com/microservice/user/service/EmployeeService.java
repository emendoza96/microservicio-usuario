package com.microservice.user.service;

import java.util.List;
import java.util.Optional;

import com.microservice.user.domain.Employee;
import com.microservice.user.error.ErrorDetail;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee);
    public Optional<Employee> getEmployeeById(Integer id);
    public Optional<Employee> getEmployeeByEmail(String email);
    public List<Employee> getAllEmployees();
    public void deleteEmployee(Integer id);
    public Boolean validateEmployee(Employee employee);
    public ErrorDetail getErrors(Employee employee);
}
