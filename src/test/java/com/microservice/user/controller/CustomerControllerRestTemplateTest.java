package com.microservice.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.microservice.user.dao.jpa.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.domain.dto.CustomerDTO;
import com.microservice.user.error.ErrorResponse;
import com.microservice.user.security.jwt.JwtUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private HttpHeaders headers;

    private Customer customer;

    @BeforeEach
    void setUp() {

        // Set up JWT token
        String token = "Bearer " + jwtUtils.generateAccessToken("emi123");
        headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

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

        construction.setCustomer(customer);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testSaveCustomer() throws Exception {
        //given

        //when
        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
            "/api/customer",
            HttpMethod.POST,
            entity,
            CustomerDTO.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        CustomerDTO newCustomer = response.getBody();

        assertThat(newCustomer.getCuit()).isEqualTo(customer.getCuit());
        assertThat(newCustomer.getBusinessName()).isEqualTo(customer.getBusinessName());
        assertThat(newCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(newCustomer.getMaxPay()).isEqualTo(customer.getMaxPay());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testSaveCustomerMissingConstruction() throws Exception {
        //given
        customer.setConstructionList(null);
        //when
        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/customer",
            HttpMethod.POST,
            entity,
            ErrorResponse.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = response.getBody();

        assertTrue(errorResponse.getError().getDetails().size() > 0);
        assertThat(errorResponse.getError().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getError().getMessage()).isNotNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetCustomer() throws Exception {
        //given
        String cuit = customer.getCuit();
        String business = customer.getBusinessName();
        customerRepository.save(customer);

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
            "/api/customer?cuit={cuit}&businessName={businessName}",
            HttpMethod.GET,
            entity,
            CustomerDTO.class,
            cuit,
            business
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        CustomerDTO newCustomer = response.getBody();
        assertThat(newCustomer.getCuit()).isEqualTo(customer.getCuit());
        assertThat(newCustomer.getBusinessName()).isEqualTo(customer.getBusinessName());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetCustomerMissingParams() throws Exception {
        //given
        customerRepository.save(customer);

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/customer",
            HttpMethod.GET,
            entity,
            ErrorResponse.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getError().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getError().getMessage()).isNotNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testDisableCustomer() throws Exception {
        //given
        Customer customerSaved = customerRepository.save(customer);

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
            "/api/customer/disable/{id}",
            HttpMethod.PUT,
            entity,
            CustomerDTO.class,
            customerSaved.getId()
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        CustomerDTO newCustomer = response.getBody();
        assertThat(newCustomer.getDischargeDate()).isNotNull();
    }

}
