package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Construction;

public interface ConstructionRepository extends JpaRepository<Construction, Integer> {

}
