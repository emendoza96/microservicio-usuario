package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;

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

    @PutMapping("edit/{id}")
    public Construction editConstruction(@PathVariable Integer id, @RequestBody Construction construction) {

        construction.setId(id);
        System.out.println(construction);

        return construction;
    }

    @GetMapping("/id/{id}")
    public Construction getConstruction(@PathVariable Integer id) {

        Construction construction = new Construction(
            null,
            null,
            null,
            null,
            0
        );

        construction.setId(id);

        return construction;
    }

    @GetMapping("/param")
    public Construction getConstructionByParams(
        @RequestParam(required = false) String client,
        @RequestParam(required = false) String constructionType
    ) {

        System.out.println(client);
        System.out.println(constructionType);

        return null;
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteConstruction(@PathVariable Integer id) {

        return true;
    }
}
