package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;
import com.microservice.user.service.ConstructionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ConstructionService constructionService;

    @GetMapping
    @ApiOperation(value = "Get constructions by parameters")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get request successfully completed"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Construction not found with the parameters provided")
    })
    public ResponseEntity<List<Construction>> getConstruction(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String constructionType
    ) {
        try {
            List<Construction> constructions = constructionService.getConstructionByParams(customerName, constructionType);
            return ResponseEntity.status(200).body(constructions);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping
    @ApiOperation(value = "Save a new construction")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New construction successfully saved"),
        @ApiResponse(code = 401, message = "Not authorized"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<Construction> saveConstruction(@RequestBody Construction construction, @RequestParam Integer customerId) {

        try {
            if(!constructionService.validateConstruction(construction, customerId)) throw new Exception("Invalid data");

            Construction newConstruction = constructionService.createConstruction(construction, customerId);
            return ResponseEntity.status(201).body(newConstruction);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }

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
    @ApiOperation(value = "Delete a construction")
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
