package com.microservice.user.service;

import java.util.List;
import java.util.Optional;

import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.dto.ConstructionDTO;
import com.microservice.user.error.ErrorDetail;

public interface ConstructionService {

    public Construction createConstruction(Construction construction, Integer customerId);
    public Optional<Construction> getConstructionById(Integer id);
    public List<Construction> getAllConstructions();
    public List<Construction> getConstructionByParams(String customerName, String constructionType);
    public void deleteConstruction(Integer id);
    public Boolean validateConstruction(Construction construction, Integer customerId);
    public ErrorDetail getErrors(Construction construction, Integer customerId);
    public List<Construction> constructionsDtoToConstructions(List<ConstructionDTO> constructionDTOs, Customer customer);
    public Construction constructionDtoToConstruction(ConstructionDTO constructionDTO, Customer customer);

}
