package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;


import com.microservice.user.domain.Customer;
import com.microservice.user.service.CustomerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping()
    @ApiOperation(value = "Get a customer by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Customer not found with the parameters provided")
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
    @ApiOperation(value = "Save a new customer")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New customer successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
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


}
