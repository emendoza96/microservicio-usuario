package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/construction")
public class ConstructionController {

    @PostMapping
    public Construction saveConstruction(@RequestBody Construction construction) {

        System.out.println(construction);

        return construction;
    }

    @PutMapping("/edit/{id}")
    public Construction editConstruction(@PathVariable Integer id, @RequestBody Construction construction) {

        construction.setId(id);
        System.out.println(construction);

        return construction;
    }

    @GetMapping
    public Construction getConstruction(
        @RequestParam Integer id,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String constructionType
    ) {

        Customer customer = new Customer(customerName, null, null, null, null);
        ConstructionType type = new ConstructionType(1, constructionType);

        Construction construction = new Construction(
            null,
            null,
            null,
            null,
            0,
            customer
        );

        construction.setId(id);
        construction.setConstructionType(type);

        return construction;
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteConstruction(@PathVariable Integer id) {

        return true;
    }
}
