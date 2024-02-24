package com.microservice.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
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

import com.microservice.user.dao.ConstructionRepository;
import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.security.jwt.JwtUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConstructionControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private HttpHeaders headers;

    private Customer customer1;
    private Customer customer2;
    private Construction construction;

    @BeforeEach
    void setUp() {

        // Set up JWT token
        String token = "Bearer " + jwtUtils.generateAccessToken("emi123");
        headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Construction construction1 = Construction.builder()
            .description("test desc")
            .latitude(30.4f)
            .longitude(40.4f)
            .direction("Test - 463")
            .area(50)
            .constructionType(new ConstructionType(1, "REPAIR"))
            .build()
        ;

        Construction construction2 = Construction.builder()
            .description("test desc 2")
            .latitude(33.4f)
            .longitude(43.4f)
            .direction("Test - 1234")
            .area(100)
            .constructionType(new ConstructionType(2, "HOUSE"))
            .build()
        ;

        customer1 = Customer.builder()
            .businessName("Test business")
            .cuit("20-32424559-7")
            .email("test@gmail.com")
            .maxPay(2000d)
            .user(new UserEntity("test123", "test123"))
            .constructionList(List.of(construction1, construction2))
            .build()
        ;

        construction1.setCustomer(customer1);
        construction2.setCustomer(customer1);

        Construction construction3 = Construction.builder()
            .description("test desc")
            .latitude(30.4f)
            .longitude(40.4f)
            .direction("Test - 463")
            .area(50)
            .constructionType(new ConstructionType(2, "HOUSE"))
            .build()
        ;

        customer2 = Customer.builder()
            .businessName("Test business")
            .cuit("20-112232349-7")
            .email("test2@gmail.com")
            .maxPay(2000d)
            .user(new UserEntity("test445", "test445"))
            .constructionList(List.of(construction3))
            .build()
        ;

        construction3.setCustomer(customer2);

        construction = Construction.builder()
            .description("main test desc")
            .latitude(35.4f)
            .longitude(41.4f)
            .direction("Main Test - 463")
            .area(510)
            .constructionType(new ConstructionType(3, "BUILDING"))
            .build()
        ;
    }

    @Test
    void testSaveConstruction() {
        //given
        Customer customerSaved = customerRepository.save(customer1);
        int customerId = customerSaved.getId();

        //when
        HttpEntity<Construction> entity = new HttpEntity<>(construction, headers);
        ResponseEntity<Construction> response = restTemplate.exchange(
            "/api/construction?customerId={id}",
            HttpMethod.POST,
            entity,
            Construction.class,
            customerId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(MediaType.APPLICATION_JSON).isEqualTo(response.getHeaders().getContentType());

        Construction result = response.getBody();

        assertThat(result.getDescription()).isEqualTo(construction.getDescription());
        assertThat(result.getDirection()).isEqualTo(construction.getDirection());
        assertThat(result.getArea()).isEqualTo(construction.getArea());
    }

    @Test
    void testDeleteConstruction() {
        //given
        Customer customerSaved = customerRepository.save(customer1);
        int constructionId = customerSaved.getConstructionList().get(0).getId();

        //when
        HttpEntity<Construction> entity = new HttpEntity<>(construction, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/construction/delete/{id}",
            HttpMethod.DELETE,
            entity,
            String.class,
            constructionId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testEditConstruction() {
        //given
        Customer customerSaved = customerRepository.save(customer1);
        int constructionId = customerSaved.getConstructionList().get(0).getId();

        //when
        HttpEntity<Construction> entity = new HttpEntity<>(construction, headers);
        ResponseEntity<Construction> response = restTemplate.exchange(
            "/api/construction/edit/{id}",
            HttpMethod.PUT,
            entity,
            Construction.class,
            constructionId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Construction result = response.getBody();
        assertThat(result.getId()).isEqualTo(constructionId);
        assertThat(result.getDescription()).isEqualTo(construction.getDescription());
        assertThat(result.getDirection()).isEqualTo(construction.getDirection());
        assertThat(result.getArea()).isEqualTo(construction.getArea());
    }

    @Test
    void testGetConstructionById() {
        //given
        customerRepository.save(customer1);
        Customer customerSaved = customerRepository.save(customer2);
        int constructionId = customerSaved.getConstructionList().get(0).getId();

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Construction[]> response = restTemplate.exchange(
            "/api/construction?id={id}",
            HttpMethod.GET,
            entity,
            Construction[].class,
            constructionId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(MediaType.APPLICATION_JSON).isEqualTo(response.getHeaders().getContentType());

        List<Construction> constructions = List.of(response.getBody());
        assertThat(constructions.size()).isGreaterThan(0);
        assertThat(constructions.get(0).getId()).isEqualTo(constructionId);
    }

    @Test
    void testGetConstructionByParams() {
        //given
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        String businessName = customer2.getBusinessName();
        String constructionType = "HOUSE";

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Construction[]> response = restTemplate.exchange(
            "/api/construction?businessName={businessName}&constructionType={constructionType}",
            HttpMethod.GET,
            entity,
            Construction[].class,
            businessName,
            constructionType
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(MediaType.APPLICATION_JSON).isEqualTo(response.getHeaders().getContentType());

        List<Construction> constructions = List.of(response.getBody());
        assertThat(constructions.size()).isGreaterThan(0);
        assertThat(constructions.get(0).getConstructionType().getType()).isEqualTo(constructionType);
    }

    @AfterEach
    void clearDB() {
        constructionRepository.deleteAll();
        customerRepository.deleteAll();
    }
}
