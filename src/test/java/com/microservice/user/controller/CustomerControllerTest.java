package com.microservice.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.dto.ConstructionDTO;
import com.microservice.user.domain.dto.SaveCustomerRequest;
import com.microservice.user.security.filters.MockJwtAuthorizationFilter;
import com.microservice.user.service.CustomerService;
import com.microservice.user.utils.MessagePropertyUtils;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private MessagePropertyUtils messageUtils;

    @InjectMocks
    private CustomerController customerController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private SaveCustomerRequest customer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        ConstructionDTO constructionDTO = new ConstructionDTO();
        constructionDTO.setArea(12);
        constructionDTO.setDescription("test");
        constructionDTO.setLatitude(30.6f);
        constructionDTO.setLongitude(32.f);
        constructionDTO.setDirection("Test - 463");

        customer = new SaveCustomerRequest();
        customer.setBusinessName("Test business");
        customer.setEmail("test@gmail.com");
        customer.setCuit("20-32424559-7");
        customer.getConstructionList().add(constructionDTO);

        // Authorization

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
            .addFilters(new MockJwtAuthorizationFilter())
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .build()
        ;
    }

    @Test
    void testDisableCustomer() throws Exception {
        //given
        int customerId = 1;
        when(customerService.disableCustomer(customerId)).thenAnswer(invocation -> {
            Customer customerAux = new Customer();
            customerAux.setId(customerId);
            customerAux.setDischargeDate(LocalDate.now());
            System.err.println(customerAux);
            return customerAux;
        });

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/customer/disable/{id}", customerId)
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.dischargeDate").exists())
        ;
    }

    @Test
    void testGetCustomer() throws Exception {
        //given
        String cuit = customer.getCuit();
        String business = customer.getBusinessName();

        when(customerService.getCustomerByParam(cuit, business)).thenAnswer(invocation -> {
            Customer customerAux = new Customer();
            customerAux.setId(1);
            customerAux.setCuit(cuit);
            customerAux.setBusinessName(business);
            System.err.println(customerAux);
            return customerAux;
        });

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/customer?cuit={cuit}&businessName={businessName}", cuit, business)
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.cuit").value(cuit))
            .andExpect(MockMvcResultMatchers.jsonPath("$.businessName").value(business))
        ;
    }

    @Test
    void testSaveCustomer() throws Exception {
        //given
        when(customerService.createCustomer(any(SaveCustomerRequest.class))).thenAnswer(invocation -> {
            SaveCustomerRequest saveCustomerRequest = invocation.getArgument(0);
            Customer customerAux = new Customer();
            customerAux.setId(1);
            customerAux.setBusinessName(saveCustomerRequest.getBusinessName());
            customerAux.setCuit(saveCustomerRequest.getCuit());
            return customerAux;
        });

        String jsonResult = objectMapper.writeValueAsString(customer);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.businessName").value(customer.getBusinessName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cuit").value(customer.getCuit()))
        ;
    }

    @Test
    void testSaveCustomerWithMissingConstruction() throws Exception {
        //given
        customer.setConstructionList(new ArrayList<>());
        String jsonResult = objectMapper.writeValueAsString(customer);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;
    }
}
