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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.dao.EmployeeRepository;
import com.microservice.user.domain.Employee;
import com.microservice.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testGetEmployee() throws Exception {
        Employee employee = employeeRepository.findById(1).orElseThrow();

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                    "/api/employee?id={id}&email={email}",
                    employee.getId(),
                    employee.getEmail()
                )
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employee.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));

    }

    @Test
    void testPutEmployee() throws Exception {
        Employee employee = employeeRepository.findById(1).orElseThrow();
        employee.setEmail("test@gmail.com");

        String employeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.put(
                    "/api/employee/edit/{id}",
                    employee.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));
    }

    @Test
    void testSaveEmployee() throws Exception {
        Employee employee = new Employee("new_employee@gmail.com", new User("newAcc", "new123"));

        String employeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.user.username").value(employee.getUser().getUsername()));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/employee/delete/{id}",
                2
            )
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }
}
