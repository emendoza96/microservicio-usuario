package com.microservice.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.UserEntity;
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

    private Customer customer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Construction construction = Construction.builder()
            .description("test desc")
            .latitude(30.4f)
            .longitude(40.4f)
            .direction("Test - 463")
            .area(50)
            .constructionType(new ConstructionType(1, "REPAIR"))
            .build()
        ;

        customer = Customer.builder()
            .businessName("Test business")
            .cuit("20-32424559-7")
            .email("test@gmail.com")
            .maxPay(2000d)
            .user(new UserEntity("test123", "test123"))
            .constructionList(List.of(construction))
            .build()
        ;

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
            customer.setId(customerId);
            customer.setDischargeDate(LocalDate.now());
            return customer;
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

        when(customerService.getCustomerByParam(cuit, business)).thenReturn(customer);

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
        when(customerService.createCustomer(any(Customer.class))).thenAnswer(invocation -> {
            Customer customerResult = invocation.getArgument(0);
            return customerResult;
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.maxPay").value(customer.getMaxPay()))
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
