package com.microservice.user.domain.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;

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
public class CustomerDTO {

    private int id;
    private String businessName;
    private String cuit;
    private String email;
    private LocalDate dischargeDate;
    private Double maxPay;
    private List<ConstructionDTO> constructions;

    public static CustomerDTO customerMapping(Customer customer) {
        List<ConstructionDTO> constructionDTOs = new ArrayList<>();

        if (customer.getConstructionList() != null) {
            for (Construction construction : customer.getConstructionList()) {
                constructionDTOs.add(ConstructionDTO.constructionMapping(construction));
            }
        }

        return CustomerDTO.builder()
            .id(customer.getId())
            .businessName(customer.getBusinessName())
            .cuit(customer.getCuit())
            .email(customer.getEmail())
            .dischargeDate(customer.getDischargeDate())
            .maxPay(customer.getMaxPay())
            .constructions(constructionDTOs)
            .build()
        ;
    }
}
