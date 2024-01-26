package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.ConstructionType;

public interface ConstructionTypeRepository extends JpaRepository<ConstructionType, Integer> {

}
