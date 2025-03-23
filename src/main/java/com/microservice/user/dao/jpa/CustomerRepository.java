package com.microservice.user.dao.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    public Optional<Customer> findByCuitOrBusinessName(String cuit, String businessName);

}
