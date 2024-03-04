package com.microservice.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.error.ErrorDetails;
import com.microservice.user.security.filters.MockJwtAuthorizationFilter;
import com.microservice.user.service.ConstructionService;
import com.microservice.user.utils.MessagePropertyUtils;

@ExtendWith(MockitoExtension.class)
public class ConstructionControllerTest {

    @Mock
    private ConstructionService constructionService;

    @Mock
    private MessagePropertyUtils messageUtils;

    @InjectMocks
    private ConstructionController constructionController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Customer customer;
    private Construction construction;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        this.construction =  Construction.builder()
            .description("main construction")
            .latitude(35.4f)
            .longitude(41.4f)
            .direction("Test - 444")
            .area(100)
            .customer(Customer.builder().id(1).build())
            .build()
        ;

        Construction construction0 = Construction.builder()
            .description("test desc")
            .latitude(30.4f)
            .longitude(40.4f)
            .direction("Test - 463")
            .area(50)
            .build()
        ;

        customer = Customer.builder()
            .id(1)
            .businessName("Test business")
            .cuit("20-32424559-7")
            .email("test@gmail.com")
            .maxPay(2000d)
            .user(new UserEntity("test123", "test123"))
            .constructionList(List.of(construction0))
            .build()
        ;

        // Authorization

        mockMvc = MockMvcBuilders.standaloneSetup(constructionController)
            .addFilters(new MockJwtAuthorizationFilter())
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .build()
        ;
    }

    @Test
    void testSaveConstruction() throws Exception {
        //given
        int customerId = 1;
        when(constructionService.getErrors(any(), any())).thenReturn(new ErrorDetails());
        when(constructionService.createConstruction(any(Construction.class), any())).thenAnswer(invocation -> {
            Construction construction = invocation.getArgument(0);
            construction.setId(customerId);
            construction.setCustomer(customer);
            return construction;
        });

        String jsonResult = objectMapper.writeValueAsString(construction);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/construction?customerId={id}", customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
    }

    @Test
    void testSaveConstructionWithMissingConstructionType() throws Exception {
        //given
        int customerId = 1;
        when(constructionService.getErrors(any(), any())).thenAnswer(invocation -> {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.getDetails().put("constructionType", "test");
            return errorDetails;
        });

        String jsonResult = objectMapper.writeValueAsString(construction);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/construction?customerId={id}", customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.details.constructionType").value("test"))
        ;
    }

    @Test
    void testDeleteConstruction() throws Exception {
        //given
        int constructionId = 1;
        when(constructionService.getConstructionById(any())).thenReturn(Optional.of(new Construction()));
        doNothing().when(constructionService).deleteConstruction(constructionId);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/construction/delete/{id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(constructionService, times(1)).deleteConstruction(constructionId);
    }

    @Test
    void testDeleteMissingConstruction() throws Exception {
        //given
        int constructionId = 9999;
        when(constructionService.getConstructionById(any())).thenThrow(new NoSuchElementException("test"));

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/construction/delete/{id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;

    }

    @Test
    void testEditConstruction() throws Exception {
        //given
        int constructionId = 1;
        construction.setId(constructionId);
        when(constructionService.getErrors(any(), any())).thenReturn(new ErrorDetails());
        when(constructionService.getConstructionById(constructionId)).thenReturn(Optional.of(construction));
        when(constructionService.createConstruction(any(Construction.class), any())).thenAnswer(invocation -> {
            Construction construction = invocation.getArgument(0);
            construction.setId(constructionId);
            construction.setArea(200);
            return construction;
        });

        String jsonResult = objectMapper.writeValueAsString(construction);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/construction/edit/{id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(constructionId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.area").value(200))
        ;
    }

    @Test
    void testEditMissingConstruction() throws Exception {
        //given
        int constructionId = 1;
        when(constructionService.getConstructionById(any())).thenThrow(new NoSuchElementException("test"));

        String jsonResult = objectMapper.writeValueAsString(construction);

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/construction/edit/{id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonResult)
        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void testGetConstructionById() throws Exception {
        //given
        int constructionId = 1;
        construction.setId(constructionId);
        when(constructionService.getConstructionById(constructionId)).thenReturn(Optional.of(construction));

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/construction?id={id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)

        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    void testGetConstructionByNotFoundId() throws Exception {
        //given
        int constructionId = 1;
        construction.setId(constructionId);
        when(constructionService.getConstructionById(constructionId)).thenReturn(Optional.empty());
        when(constructionService.getConstructionByParams(any(), any())).thenReturn(new ArrayList<>());

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/construction?id={id}", constructionId)
            .contentType(MediaType.APPLICATION_JSON)

        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
        ;
    }


    @Test
    void testGetConstructionByParams() throws Exception {
        //given
        when(constructionService.getConstructionByParams(any(), any())).thenReturn(List.of(construction));

        //when
        ResultActions response = mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/construction?businessName={businessName}&constructionType={constructionType}",
                "TEST", "TEST"
            )
            .contentType(MediaType.APPLICATION_JSON)

        );

        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())

        ;
    }
}
