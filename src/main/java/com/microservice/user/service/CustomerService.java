package com.microservice.user.service;


import java.util.List;

import com.microservice.user.domain.Customer;
import com.microservice.user.error.ErrorDetails;

public interface CustomerService {

    public List<Customer> getAllCustomers();
    public Customer getCustomerById(Integer id);
    public Customer getCustomerByParam(String cuit, String businessName);
    public Customer createCustomer(Customer customer);
    public Customer editCustomer(Customer customer);
    public Customer enableCustomer(Customer customer);
    public Customer disableCustomer(Integer id);
    public ErrorDetails getErrors(Customer customer);

}
