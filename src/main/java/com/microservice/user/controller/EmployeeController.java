package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.dao.EmployeeRepository;
import com.microservice.user.domain.Employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/employee")
@Api(value = "EmployeeRest")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping
    @ApiOperation(value = "Get a employee by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found with the parameters provided")
    })
    public Employee getEmployee(
        @RequestParam Integer id,
        @RequestParam(required = false) String email
    ) {

        return employeeRepo.findById(id).get();
    }

    @PostMapping
    @ApiOperation(value = "Save a new employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New employee successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    public Employee postEmployee(@RequestBody Employee employee) {

        employeeRepo.save(employee);

        return employee;
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit a employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully edited"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found")
    })
    public Employee putEmployee(@PathVariable Integer id, @RequestBody Employee employee) {

        employee.setId(id);

        return employee;
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete a employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully deleted"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found")
    })
    public Boolean deleteEmployee(@PathVariable Integer id) {

        return true;
    }
}
