package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Customer;
import com.microservice.user.error.ErrorDetails;
import com.microservice.user.error.ErrorResponse;
import com.microservice.user.service.CustomerService;
import com.microservice.user.utils.MessagePropertyUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessagePropertyUtils messageUtils;

    @GetMapping()
    @Operation(summary = "Get a customer by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Customer not found with the parameters provided")
    })
    public ResponseEntity<?> getCustomer(
        @RequestParam(required = false) String cuit,
        @RequestParam(required = false) String businessName
    ) {

        try {

            if(cuit == null && businessName == null) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
                errorDetails.setMessage(messageUtils.getMessage("missing_params", "cuit or business name"));
                return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
            }

            Customer customer = customerService.getCustomerByParam(cuit, businessName);
            return ResponseEntity.ok().body(customer);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Customers not found")
    })
    public ResponseEntity<?> getCustomer() {

        try {
            List<Customer> customer = customerService.getAllCustomers();
            return ResponseEntity.ok().body(customer);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }

    @PostMapping
    @Operation(summary = "Save a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New customer successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {

        try {

            ErrorDetails customerErrors = customerService.getErrors(customer);

            if(!customerErrors.getDetails().isEmpty()) {
                customerErrors.setCode(HttpStatus.BAD_REQUEST.value());
                customerErrors.setMessage(messageUtils.getMessage("missing_data_error"));

                return ResponseEntity.badRequest().body(new ErrorResponse(customerErrors));
            }

            Customer newCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(201).body(newCustomer);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }

    }

    @PutMapping("/disable/{id}")
    @Operation(summary = "Disable customer by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer successfully disabled"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> disableCustomer(@PathVariable Integer id) {

        try {
            Customer customer = customerService.disableCustomer(id);
            return ResponseEntity.ok().body(customer);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }

}
