package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Customer;
import com.microservice.user.service.CustomerService;
import com.microservice.user.utils.MessagePropertyUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

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
    public ResponseEntity<Customer> getCustomer(
        @RequestParam(required = false) String cuit,
        @RequestParam(required = false) String businessName
    ) {

        try {

            Customer customer = customerService.getCustomerByParam(cuit, businessName);

            return ResponseEntity.ok().body(customer);
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
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {

        HashMap<String, Object> responseHashMap = new HashMap<>();

        try {

            HashMap<String, String> customerErrors = customerService.getErrors(customer);

            if(!customerErrors.isEmpty()) {
                HashMap<String, Object> errorHashMap = new HashMap<>();

                errorHashMap.put("details", customerErrors);
                errorHashMap.put("statusCode", HttpStatus.BAD_REQUEST.value());
                errorHashMap.put("message", messageUtils.getMessage("missing_data_error"));
                responseHashMap.put("error", errorHashMap);

                return ResponseEntity.badRequest().body(responseHashMap);
            }

            Customer newCustomer = customerService.createCustomer(customer);
            responseHashMap.put("message", messageUtils.getMessage("new_entity_created", "customer"));
            responseHashMap.put("body", newCustomer);
            responseHashMap.put("code", HttpStatus.CREATED.value());

            return ResponseEntity.status(201).body(responseHashMap);
        } catch (Exception e) {
            responseHashMap.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseHashMap);
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
            return ResponseEntity.ok().body(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
