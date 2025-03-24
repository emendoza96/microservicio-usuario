package com.microservice.user.domain.dto;

import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConstructionDTO {

    private String description;
    private String direction;
    private Float latitude;
    private Float longitude;
    private int area;
    private ConstructionType.ConstructionTypeEnum type;
    private Integer customerId;

    public static ConstructionDTO constructionMapping(Construction construction) {
        return ConstructionDTO.builder()
            .area(construction.getArea())
            .description(construction.getDescription())
            .direction(construction.getDirection())
            .type(construction.getConstructionType().getType())
            .latitude(construction.getLatitude())
            .longitude(construction.getLongitude())
            .customerId(construction.getCustomer().getId())
            .build()
        ;
    }
}
