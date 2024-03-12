package com.microservice.user.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.EmployeeRepository;
import com.microservice.user.domain.Employee;
import com.microservice.user.error.ErrorDetail;
import com.microservice.user.service.EmployeeService;
import com.microservice.user.utils.MessagePropertyUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessagePropertyUtils messageUtils;

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Boolean validateEmployee(Employee employee) {

        return
            employee.getEmail() != null
            && employee.getUser().getPassword() != null
            && employee.getUser().getUsername() != null;

    }

    @Override
    public ErrorDetail getErrors(Employee employee) {
        ErrorDetail errorDetails = new ErrorDetail();

        if (employee.getEmail() == null) {
            errorDetails.getDetails().put("email", messageUtils.getMessage("missing_email_error"));
        }

        if (employee.getUser().getPassword() == null || employee.getUser().getUsername() == null) {
            errorDetails.getDetails().put("user", messageUtils.getMessage("missing_user_error"));
        }

        return errorDetails;
    }

}
