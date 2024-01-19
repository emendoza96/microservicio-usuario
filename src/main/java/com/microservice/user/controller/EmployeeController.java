package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Employee;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/employee")
@Api(value = "EmployeeRest")
public class EmployeeController {


    @GetMapping
    public Employee getEmployee(
        @RequestParam Integer id,
        @RequestParam(required = false) String email
    ) {

        Employee employee = new Employee(email, null);
        employee.setId(id);

        return employee;
    }

    @PostMapping
    public Employee postEmployee(@RequestBody Employee employee) {

        System.out.println(employee);

        return employee;
    }

    @PutMapping("/edit/{id}")
    public Employee putEmployee(@PathVariable Integer id, @RequestBody Employee employee) {

        employee.setId(id);

        return employee;
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteEmployee(@PathVariable Integer id) {

        return true;
    }
}
