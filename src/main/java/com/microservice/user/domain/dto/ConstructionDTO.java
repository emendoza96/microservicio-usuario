package com.microservice.user.domain.dto;

import com.microservice.user.domain.Construction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConstructionDTO {

    private Integer id;
    private String description;
    private Float latitude;
    private Float longitude;
    private String direction;
    private Integer area;
    private String constructionType;
    private Integer customerId;

    public static ConstructionDTO constructionMapping(Construction construction) {
        return ConstructionDTO.builder()
            .id(construction.getId())
            .area(construction.getArea())
            .description(construction.getDescription())
            .direction(construction.getDirection())
            .constructionType(construction.getConstructionType().getType())
            .latitude(construction.getLatitude())
            .longitude(construction.getLongitude())
            .customerId(construction.getCustomer().getId())
            .build()
        ;
    }
}
