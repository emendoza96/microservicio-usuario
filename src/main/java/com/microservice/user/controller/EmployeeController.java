package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Employee;
import com.microservice.user.error.ErrorDetails;
import com.microservice.user.error.ErrorResponse;
import com.microservice.user.service.EmployeeService;
import com.microservice.user.utils.MessagePropertyUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private MessagePropertyUtils messageUtils;

    @GetMapping
    @Operation(summary = "Get a employee by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Employee not found with the parameters provided")
    })
    public ResponseEntity<?> getEmployee(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String email
    ) {

        try {
            Optional<Employee> employee =
                id != null ?
                employee = employeeService.getEmployeeById(id) : employeeService.getEmployeeByEmail(email)
            ;

            return ResponseEntity.ok().body(employee.orElseThrow());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }

    @PostMapping
    @Operation(summary = "Save a new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New employee successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {

        try {
            ErrorDetails errorDetails = employeeService.getErrors(employee);

            if (!errorDetails.getDetails().isEmpty()) {
                errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
                errorDetails.setMessage(messageUtils.getMessage("missing_data_error"));
                return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
            }

            Employee newEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.status(201).body(newEmployee);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
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
    public ResponseEntity<?> putEmployee(@PathVariable Integer id, @RequestBody Employee employee) {

        try {
            ErrorDetails errorDetails = employeeService.getErrors(employee);

            if (!errorDetails.getDetails().isEmpty()) {
                errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
                errorDetails.setMessage(messageUtils.getMessage("missing_data_error"));
                return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
            }

            Employee employeeOld = employeeService.getEmployeeById(id).orElseThrow();
            employee.setId(id);
            employee.getUser().setId(employeeOld.getUser().getId());

            Employee employeeResult = employeeService.saveEmployee(employee);
            return ResponseEntity.ok().body(employeeResult);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
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
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {

        try {
            employeeService.getEmployeeById(id).orElseThrow();
            employeeService.deleteEmployee(id);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }
}
