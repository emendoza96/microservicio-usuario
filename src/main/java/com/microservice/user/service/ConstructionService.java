package com.microservice.user.service;

import java.util.List;
import java.util.Optional;

import com.microservice.user.domain.Construction;
import com.microservice.user.error.ErrorDetails;

public interface ConstructionService {

    public Construction createConstruction(Construction construction, Integer customerId);
    public Optional<Construction> getConstructionById(Integer id);
    public List<Construction> getAllConstructions();
    public List<Construction> getConstructionByParams(String customerName, String constructionType);
    public void deleteConstruction(Integer id);
    public Boolean validateConstruction(Construction construction, Integer customerId);
    public ErrorDetails getErrors(Construction construction, Integer customerId);

}
