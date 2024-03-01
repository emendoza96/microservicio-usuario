package com.microservice.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.ConstructionType;

public interface ConstructionTypeRepository extends JpaRepository<ConstructionType, Integer> {

    public Optional<ConstructionType> findByType(String type);

}
