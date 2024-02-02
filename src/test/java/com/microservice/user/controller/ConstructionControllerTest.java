package com.microservice.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.dao.ConstructionRepository;
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

    @Autowired
    private ConstructionRepository constructionRepository;

    @Test
    void testEditConstruction() throws Exception {
        Construction construction = constructionRepository.findById(1).orElseThrow();
        construction.setCustomer(null);
        construction.setArea(4324);

        String constructionJson = objectMapper.writeValueAsString(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.put(
                    "/api/construction/edit/{id}",
                    construction.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(constructionJson)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(construction.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.area").value(construction.getArea()));
    }

    @Test
    void testGetConstructionByParams() throws Exception {

        Construction construction = constructionRepository.findById(1).orElseThrow();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/construction?businessName={businessName}&constructionType={type}",
                    construction.getCustomer().getBusinessName(),
                    construction.getConstructionType().getType()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customer.businessName").value(construction.getCustomer().getBusinessName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].constructionType.type").value(construction.getConstructionType().getType()));
    }

    @Test
    void testGetConstructionById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    "/api/construction?id={id}",
                    1
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1));
    }

    @Test
    void testSaveConstruction() throws Exception {
        Construction construction = new Construction("New construction", 36.541f, 40.534f, "Street 234", 55);
        construction.setConstructionType(new ConstructionType(1, "REPAIR"));

        Customer customer = customerRepository.findById(1).orElseThrow();

        String constructionJson = objectMapper.writeValueAsString(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.post(
                    "/api/construction?customerId={customerId}",
                    customer.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(constructionJson)
            )
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());
    }

    @Test
    void testDeleteConstruction() throws Exception {
        Integer construction_id = 2;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(
                    "/api/construction/delete/{id}",
                    construction_id
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }
}
