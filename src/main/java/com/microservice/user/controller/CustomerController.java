package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @GetMapping()
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
    public Customer saveCustomer(@RequestBody Customer customer) {

        System.out.println(customer);

        return customer;
    }


}
