package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Employee;
import com.microservice.user.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "EmployeeRest")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get a employee by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Employee not found with the parameters provided")
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
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @Operation(summary = "Save a new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New employee successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {

        try {
            if(!employeeService.validateEmployee(employee)) {
                throw new Exception("Missing fields");
            };

            Employee newEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.status(201).body(newEmployee);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit a employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee successfully edited"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
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
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee successfully deleted"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Object> deleteEmployee(@PathVariable Integer id) {

        try {
            employeeService.getEmployeeById(id).orElseThrow();
            employeeService.deleteEmployee(id);

            return ResponseEntity.status(200).build();
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
