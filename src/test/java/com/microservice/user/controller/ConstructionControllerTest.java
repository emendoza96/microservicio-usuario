package com.microservice.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;

@SpringBootTest
@AutoConfigureMockMvc
public class ConstructionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testDeleteConstruction() {

    }

    @Test
    void testEditConstruction() {

    }

    @Test
    void testGetConstruction() {

    }

    @Test
    void testSaveConstruction() throws Exception {
        Construction construction = new Construction("New construction", 36.541f, 40.534f, "Street 234", 55);
        Customer customer = customerRepository.save(CustomerControllerTest.getCustomer());
        construction.setCustomer(customer);
        construction.setConstructionType(new ConstructionType(1, "REPAIR"));

        String customerJson = objectMapper.writeValueAsString(construction);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/construction?customerId={customerId}" , construction.getCustomer().getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerJson))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

    }
}
