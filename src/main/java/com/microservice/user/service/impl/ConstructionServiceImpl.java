package com.microservice.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.jpa.ConstructionRepository;
import com.microservice.user.dao.jpa.ConstructionTypeRepository;
import com.microservice.user.dao.jpa.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.ConstructionType;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.dto.ConstructionDTO;
import com.microservice.user.error.ErrorDetail;
import com.microservice.user.service.ConstructionService;
import com.microservice.user.utils.MessagePropertyUtils;

@Service
public class ConstructionServiceImpl implements ConstructionService {

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private ConstructionTypeRepository constructionTypeRepository;

    @Autowired
    private MessagePropertyUtils messageUtils;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Construction createConstruction(Construction construction, Integer customerId) {

        construction.setCustomer(customerRepository.findById(customerId).get());

        return constructionRepository.save(construction);
    }

    @Override
    public Optional<Construction> getConstructionById(Integer id) {
        return constructionRepository.findById(id);
    }

    @Override
    public List<Construction> getAllConstructions() {
        return constructionRepository.findAll();
    }

    @Override
    public void deleteConstruction(Integer id) {
        constructionRepository.deleteById(id);
    }

    @Override
    public Boolean validateConstruction(Construction construction, Integer customerId) {

        try {
            ConstructionType constructionType = construction.getConstructionType();
            ConstructionType constructionTypeSaved = constructionTypeRepository.findById(constructionType.getId()).orElseThrow();
            customerRepository.findById(customerId).orElseThrow();
            return constructionType.getType().equals(constructionTypeSaved.getType());
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public ErrorDetail getErrors(Construction construction, Integer customerId) {
        ErrorDetail errorDetails = new ErrorDetail();

        if(construction.getConstructionType() == null || construction.getConstructionType().getType() == null) {
            errorDetails.getDetails().put("constructionType", messageUtils.getMessage("missing_construction_type_error"));
        } else if (!constructionTypeRepository.findByType(construction.getConstructionType().getType()).isPresent()) {
            errorDetails.getDetails().put("constructionType", messageUtils.getMessage("invalid_construction_type_error"));
        }

        if(!customerRepository.existsById(customerId)) {
            errorDetails.getDetails().put("customer", messageUtils.getMessage("missing_customer_error"));
        }

        return errorDetails;
    }

    @Override
    public List<Construction> getConstructionByParams(String customerName, String constructionType) {
        return constructionRepository.findByBusinessNameAndType(customerName, constructionType);
    }

    @Override
    public List<Construction> constructionsDtoToConstructions(List<ConstructionDTO> constructionDTOs, Customer customer) {
        List<Construction> constructions = new ArrayList<>();

        for (ConstructionDTO constructionDto : constructionDTOs) {
            constructions.add(this.constructionDtoToConstruction(constructionDto, customer));
        }

        return constructions;
    }

    @Override
    public Construction constructionDtoToConstruction(ConstructionDTO constructionDTO, Customer customer) {
        Construction newConstruction = new Construction();
        newConstruction.setDescription(constructionDTO.getDescription());
        newConstruction.setArea(constructionDTO.getArea());
        newConstruction.setDirection(constructionDTO.getDirection());
        newConstruction.setLatitude(constructionDTO.getLatitude());
        newConstruction.setLongitude(constructionDTO.getLongitude());
        ConstructionType type = constructionTypeRepository.findByType(constructionDTO.getType()).orElseThrow(() -> new IllegalArgumentException("Construction Type not found"));
        newConstruction.setConstructionType(type);
        newConstruction.setCustomer(customer);
        return newConstruction;
    }

}
