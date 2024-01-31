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
    void testDeleteConstruction() throws Exception {
        Construction construction = getConstruction();

        constructionRepository.save(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(
                    "/api/construction/delete/{id}",
                    construction.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    void testEditConstruction() throws Exception {
        Construction construction = getConstruction();

        Customer customer = CustomerControllerTest.getCustomer();

        for (Construction constructionAux : customer.getConstructionList()) {
            constructionAux.setCustomer(customer);
        }

        customer = customerRepository.save(customer);

        String constructionJson = objectMapper.writeValueAsString(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.put(
                    "/api/construction/edit/{id}",
                    customer.getConstructionList().get(0).getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(constructionJson)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.area").value(construction.getArea()));
    }

    @Test
    void testGetConstructionByParams() throws Exception {
        Construction construction = getConstruction();

        Customer customer = customerRepository.save(CustomerControllerTest.getCustomer());
        construction.setCustomer(customer);

        constructionRepository.save(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/construction?businessName={businessName}&constructionType={type}",
                    customer.getBusinessName(),
                    construction.getConstructionType().getType()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customer.businessName").value(customer.getBusinessName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].constructionType.type").value(construction.getConstructionType().getType()));
    }

    @Test
    void testGetConstructionById() throws Exception {
        Construction construction = getConstruction();

        Customer customer = customerRepository.save(CustomerControllerTest.getCustomer());
        construction.setCustomer(customer);

        Construction constructionSaved = constructionRepository.save(construction);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    "/api/construction?id={id}",
                    construction.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(constructionSaved.getId())
        );
    }

    @Test
    void testSaveConstruction() throws Exception {
        Construction construction = getConstruction();

        Customer customer = customerRepository.save(CustomerControllerTest.getCustomer());

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
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber()
        );

    }

    public static Construction getConstruction() {
        Construction construction = new Construction("New construction", 36.541f, 40.534f, "Street 234", 55);
        construction.setConstructionType(new ConstructionType(1, "REPAIR"));
        return construction;
    }
}
