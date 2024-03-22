package com.microservice.user.domain.dto;

import java.time.LocalDate;
import java.util.List;

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
    private List<ConstructionDTO> constructions;

    public static CustomerDTO customerMapping(Customer customer) {
        return CustomerDTO.builder()
            .id(customer.getId())
            .businessName(customer.getBusinessName())
            .cuit(customer.getCuit())
            .email(customer.getEmail())
            .dischargeDate(customer.getDischargeDate())
            .constructions(
                customer.getConstructionList().stream().map(c -> {
                    return ConstructionDTO.constructionMapping(c);
                }).toList()
            )
            .build()
        ;
    }
}
