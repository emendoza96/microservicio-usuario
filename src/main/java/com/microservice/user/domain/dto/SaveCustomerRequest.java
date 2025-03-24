package com.microservice.user.domain.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SaveCustomerRequest {

    private String businessName;
    private String cuit;
    private String email;
    @NotEmpty
    private List<ConstructionDTO> constructionList = new ArrayList<>();

}
