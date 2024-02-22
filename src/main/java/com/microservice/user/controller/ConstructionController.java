package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;
import com.microservice.user.service.ConstructionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
@Tag(name = "ConstructionRest")
public class ConstructionController {

    @Autowired
    private ConstructionService constructionService;

    @GetMapping
    @Operation(summary = "Get constructions by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get request successfully completed"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Construction not found with the parameters provided")
    })
    public ResponseEntity<List<Construction>> getConstruction(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String businessName,
        @RequestParam(required = false) String constructionType
    ) {
        try {
            List<Construction> constructions = new ArrayList<>();

            if (id != null) {
                Construction construction = constructionService.getConstructionById(id).orElse(null);
                if (construction != null) constructions.add(construction);
            }

            if (constructions.size() == 0) {
                constructions.addAll(constructionService.getConstructionByParams(businessName, constructionType));
            }

            return ResponseEntity.status(200).body(constructions);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping
    @Operation(summary = "Save a new construction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New construction successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
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
    @Operation(summary = "Edit a construction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction successfully edited"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Construction not found")
    })
    public ResponseEntity<Construction> editConstruction(@PathVariable Integer id, @RequestBody Construction construction) {

        try {
            Construction constructionToEdit = constructionService.getConstructionById(id).orElseThrow();
            construction.setId(id);

            Construction constructionResult = constructionService.createConstruction(construction, constructionToEdit.getCustomer().getId());
            return ResponseEntity.status(200).body(constructionResult);
        } catch (NoSuchElementException e){
            System.err.println(e);
            // Status 204 - no content
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a construction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construction successfully deleted"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Construction not found")
    })
    public ResponseEntity<Object> deleteConstruction(@PathVariable Integer id) {

        try {
            constructionService.deleteConstruction(id);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
