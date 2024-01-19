package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/construction")
@Api(value = "ConstructionRest")
public class ConstructionController {


    @GetMapping
    @ApiOperation(value = "Get a construction by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Construction not found with the parameters provided")
    })
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


    @PostMapping
    @ApiOperation(value = "Save a new construction")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Construction successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    public Construction saveConstruction(@RequestBody Construction construction) {

        System.out.println(construction);

        return construction;
    }


    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit a construction")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Construction successfully edited"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Construction not found")
    })
    public Construction editConstruction(@PathVariable Integer id, @RequestBody Construction construction) {

        construction.setId(id);
        System.out.println(construction);

        return construction;
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Edit a construction")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Construction successfully deleted"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Construction not found")
    })
    public Boolean deleteConstruction(@PathVariable Integer id) {

        return true;
    }
}
