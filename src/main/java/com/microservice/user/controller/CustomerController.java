package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Customer;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @GetMapping()
    @ApiOperation(value = "Get a customer by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Customer not found with the parameters provided")
    })
    public Customer getCustomer(
        @RequestParam String cuit,
        @RequestParam(required = false) String businessName
    ) {

        Customer customer = new Customer(
            businessName,
            cuit,
            null,
            null,
            null
        );

        return customer;
    }

    @PostMapping
    @ApiOperation(value = "Save a new customer")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New customer successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    public Customer saveCustomer(@RequestBody Customer customer) {

        System.out.println(customer);

        return customer;
    }


}
