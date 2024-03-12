package com.microservice.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.ActiveProfiles;

import com.microservice.user.dao.EmployeeRepository;
import com.microservice.user.domain.Employee;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.security.jwt.JwtUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private HttpHeaders headers;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        String token = "Bearer " + jwtUtils.generateAccessToken("emi123");
        headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        employee1 = Employee.builder()
            .email("employee1@gmail.com")
            .user(new UserEntity("test-user", "1234"))
            .build()
        ;

        employee2 = Employee.builder()
            .email("employee2@gmail.com")
            .user(new UserEntity("test-user-2", "1234"))
            .build()
        ;
    }

    @Test
    void testSaveEmployee() {
        //given

        //when
        HttpEntity<Employee> entity = new HttpEntity<>(employee1, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
            "/api/employee",
            HttpMethod.POST,
            entity,
            Employee.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

    }

    @Test
    void testDeleteEmployee() {
        //given
        Employee employeeSaved = employeeRepository.save(employee1);
        int employeeId = employeeSaved.getId();

        employeeRepository.save(employee2);

        //when
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/employee/delete/{id}",
            HttpMethod.DELETE,
            entity,
            String.class,
            employeeId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetEmployee() {
        //given
        Employee employeeSaved = employeeRepository.save(employee1);
        int employeeId = employeeSaved.getId();

        employeeRepository.save(employee2);

        //when
        HttpEntity<Employee> entity = new HttpEntity<>(headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
            "/api/employee?id={id}",
            HttpMethod.GET,
            entity,
            Employee.class,
            employeeId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        Employee employee = response.getBody();
        assertThat(employee.getId()).isEqualTo(employeeId);
        assertThat(employee.getEmail()).isEqualTo(employeeSaved.getEmail());
    }

    @Test
    void testPutEmployee() {
        //given
        Employee employeeSaved = employeeRepository.save(employee1);
        int employeeId = employeeSaved.getId();

        //when
        HttpEntity<Employee> entity = new HttpEntity<>(employee2, headers);
        ResponseEntity<Employee> response = restTemplate.exchange(
            "/api/employee/edit/{id}",
            HttpMethod.PUT,
            entity,
            Employee.class,
            employeeId
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        Employee employee = response.getBody();
        assertThat(employee.getId()).isEqualTo(employeeId);
        assertThat(employee.getEmail()).isEqualTo(employee2.getEmail());
    }

    @AfterEach
    void clearDB() {
        employeeRepository.deleteAll();
    }
}
