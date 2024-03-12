package com.microservice.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import com.microservice.user.domain.Employee;
import com.microservice.user.domain.UserEntity;
import com.microservice.user.error.ErrorDetail;
import com.microservice.user.security.filters.MockJwtAuthorizationFilter;
import com.microservice.user.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

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

        // Authorization

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
            .addFilters(new MockJwtAuthorizationFilter())
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .build()
        ;
    }

    @Test
    void testSaveEmployee() throws Exception {
        //given
        when(employeeService.getErrors(any(Employee.class))).thenReturn(new ErrorDetail());
        when(employeeService.saveEmployee(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(1);
            employee.getUser().setId(1);
            return employee;
        });

        String json = objectMapper.writeValueAsString(employee1);

        //when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        );

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("employee1@gmail.com"))
        ;
    }

    @Test
    void testDeleteEmployee() throws Exception {
        //given
        int employeeId = 1;
        employee1.setId(employeeId);
        when(employeeService.getEmployeeById(any())).thenReturn(Optional.of(employee1));
        doNothing().when(employeeService).deleteEmployee(employeeId);

        //when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/employee/delete/{id}", employeeId)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void testGetEmployeeById() throws Exception {
        //given
        int employeeId = 1;
        employee1.setId(employeeId);
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee1));

        //when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/employee?id={id}", employeeId)
        );

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeId))
        ;
    }

    @Test
    void testGetEmployeeByEmail() throws Exception {
        //given
        String email = employee1.getEmail();
        when(employeeService.getEmployeeByEmail(email)).thenReturn(Optional.of(employee1));

        //when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/employee?email={email}", email)
        );

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
        ;
    }

    @Test
    void testPutEmployee() throws Exception {
        //given
        int employeeId = 1;
        employee1.setId(employeeId);
        when(employeeService.getErrors(any(Employee.class))).thenReturn(new ErrorDetail());
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee1));
        when(employeeService.saveEmployee(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            return employee;
        });

        String json = objectMapper.writeValueAsString(employee2);

        //when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/employee/edit/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        );

        //then
        result.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee2.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(employee2.getUser().getUsername()))
        ;
    }
}
