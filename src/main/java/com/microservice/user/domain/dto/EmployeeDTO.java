package com.microservice.user.domain.dto;

import com.microservice.user.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeDTO {

    private int id;
    private String email;

    public static EmployeeDTO employeeMapping(Employee employee) {
        return EmployeeDTO.builder()
            .id(employee.getId())
            .email(employee.getEmail())
            .build()
        ;
    }
}
