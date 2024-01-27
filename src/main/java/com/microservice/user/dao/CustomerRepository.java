package com.microservice.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.domain.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    public Customer findByCuitOrBusinessName(String cuit, String businessName);

}
