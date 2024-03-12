package com.microservice.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.domain.Construction;
import com.microservice.user.error.ErrorDetail;
import com.microservice.user.error.ErrorResponse;
import com.microservice.user.service.ConstructionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getConstruction(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String businessName,
        @RequestParam(required = false) String constructionType
    ) {
        try {
            List<Construction> constructions = new ArrayList<>();

            if (id != null) {
                Optional<Construction> construction = constructionService.getConstructionById(id);
                if (construction.isPresent()) constructions.add(construction.get());
            }

            if (constructions.isEmpty()) {
                constructions.addAll(constructionService.getConstructionByParams(businessName, constructionType));
            }

            return ResponseEntity.ok().body(constructions);
        } catch (Exception e) {
            ErrorDetail errorDetails = new ErrorDetail();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }

    }


    @PostMapping
    @Operation(summary = "Save a new construction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New construction successfully saved"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> saveConstruction(@Valid @RequestBody Construction construction, @RequestParam Integer customerId) {

        try {
            Construction newConstruction = constructionService.createConstruction(construction, customerId);
            return ResponseEntity.status(201).body(newConstruction);
        } catch (Exception e) {
            ErrorDetail errorDetails = new ErrorDetail();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return  ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
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
    public ResponseEntity<?> editConstruction(@PathVariable Integer id, @Valid @RequestBody Construction construction) {

        try {

            Construction constructionToEdit = constructionService.getConstructionById(id).orElseThrow();
            int customerId = constructionToEdit.getCustomer().getId();

            construction.setId(id);
            Construction constructionResult = constructionService.createConstruction(construction, customerId);
            return ResponseEntity.ok().body(constructionResult);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            ErrorDetail errorDetails = new ErrorDetail();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return  ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
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
    public ResponseEntity<?> deleteConstruction(@PathVariable Integer id) {

        try {
            constructionService.getConstructionById(id).orElseThrow();
            constructionService.deleteConstruction(id);
            return ResponseEntity.ok().body("Construction deleted with id=" + id);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            ErrorDetail errorDetails = new ErrorDetail();
            errorDetails.setCode(HttpStatus.BAD_REQUEST.value());
            errorDetails.setMessage(e.getMessage());
            return  ResponseEntity.badRequest().body(new ErrorResponse(errorDetails));
        }
    }
}
