package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Employee;
import com.microservice.user.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private EmployeeService employeeService;

    @GetMapping
    @ApiOperation(value = "Get a employee by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found with the parameters provided")
    })
    public ResponseEntity<Employee> getEmployee(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String email
    ) {

        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            if(!employee.isPresent()) {
                employee = employeeService.getEmployeeByEmail(email);
            }

            return ResponseEntity.status(200).body(employee.orElseThrow());
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Save a new employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New employee successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {

        try {
            if(!employeeService.validateEmployee(employee)) {
                throw new Exception("Missing fields");
            };

            Employee newEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.status(200).body(newEmployee);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit a employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully edited"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found")
    })
    public ResponseEntity<Employee> putEmployee(@PathVariable Integer id, @RequestBody Employee employee) {

        try {
            if(!employeeService.validateEmployee(employee)) {
                throw new Exception("Missing fields");
            };

            employeeService.getEmployeeById(id).orElseThrow();
            employee.setId(id);

            Employee employeeResult = employeeService.saveEmployee(employee);
            return ResponseEntity.status(200).body(employeeResult);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete a employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully deleted"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Employee not found")
    })
    public ResponseEntity<Object> deleteEmployee(@PathVariable Integer id) {

        try {
            employeeService.getEmployeeById(id).orElseThrow();
            employeeService.deleteEmployee(id);

            return ResponseEntity.status(200).build();
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
