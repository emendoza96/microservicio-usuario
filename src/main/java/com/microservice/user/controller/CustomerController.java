package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;


import com.microservice.user.domain.Customer;
import com.microservice.user.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping()
    @Operation(summary = "Get a customer by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Customer not found with the parameters provided")
    })
    public ResponseEntity<Customer> getCustomer(
        @RequestParam(required = false) String cuit,
        @RequestParam(required = false) String businessName
    ) {

        try {

            Customer customer = customerService.getCustomerByParam(cuit, businessName);

            return ResponseEntity.status(200).body(customer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @Operation(summary = "Save a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New customer successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {

        try {

            if(!customerService.validateCustomer(customer)) {
                throw new Exception("Missing fields");
            };

            Customer newCustomer = customerService.createCustomer(customer);

            return ResponseEntity.status(201).body(newCustomer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/disable/{id}")
    @Operation(summary = "Disable customer by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer successfully disabled"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Customer> disableCustomer(@PathVariable Integer id) {

        try {
            Customer customer = customerService.disableCustomer(id);
            return ResponseEntity.status(200).body(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
