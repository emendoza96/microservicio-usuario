package com.microservice.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testGetCustomerByCuit() throws Exception {

        Customer customer = customerRepository.findById(1).orElseThrow();

        // Perform the GET request
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    "/api/customer?cuit={cuit}",
                    customer.getCuit()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.cuit").value(customer.getCuit()));
    }

    @Test
    void testSaveCustomer() throws Exception {

        Customer customer = getCustomer();

        // Convert to json
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.businessName").value(customer.getBusinessName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(customer.getUser().getUsername()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.password").value(customer.getUser().getPassword()));
    }

    @Test
    void testSaveCustomerMissingUser() throws Exception {

        Customer customer = getCustomer();

        // Set user to null
        customer.setUser(null);

        // Convert to json
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void testDisableCustomer() throws Exception {

        Customer customer = customerRepository.findById(1).orElseThrow();

        mockMvc.perform(
                MockMvcRequestBuilders.put(
                    "/api/customer/disable/{id}",
                    customer.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        Customer customerDischarged = customerRepository.findById(customer.getId()).orElse(null);

        assert customerDischarged.getDischargeDate() != null;

    }

    public static Customer getCustomer() {

        User user = new User("emi123", "342mie");
        Construction construction = new Construction("test", 41.1f, 32.1f, "Buenos Aires 123", 32);

        Customer customer = new Customer(
            "El rancho del Emi",
            "20-3890823-7",
            "emiliano344@gmail.com",
            false,
            null
        );

        customer.setUser(user);
        customer.getConstructionList().add(construction);

        return customer;
    }
}
