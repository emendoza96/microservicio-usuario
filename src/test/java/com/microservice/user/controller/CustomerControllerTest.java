package com.microservice.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.microservice.user.domain.Customer;
import com.microservice.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCustomer() {


    }

    @Test
    void testSaveCustomer() throws Exception {

        User user = new User("emi123", "342mie");

        Customer customer = new Customer(
            "El rancho del Emi",
            "20-3890823-7",
            "emiliano344@gmail.com",
            false,
            null
        );
        customer.setUser(user);

        // Convert to json
        String customerJson = objectMapper.writeValueAsString(customer);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.businessName").value(customer.getBusinessName()))
            .andExpect(jsonPath("$.user.username").value(user.getUsername()))
            .andExpect(jsonPath("$.user.password").value(user.getPassword()))
            .andReturn();
    }
}
