package com.microservice.user.service;


import java.util.List;

import com.microservice.user.domain.Customer;
import com.microservice.user.domain.dto.CustomerDTO;
import com.microservice.user.domain.dto.SaveCustomerRequest;
import com.microservice.user.error.ErrorDetail;

public interface CustomerService {

    public List<CustomerDTO> getAllCustomers();
    public Customer getCustomerById(Integer id);
    public Customer getCustomerByParam(String cuit, String businessName);
    public Customer createCustomer(SaveCustomerRequest customer);
    public Customer editCustomer(Customer customer);
    public Customer enableCustomer(Customer customer);
    public Customer disableCustomer(Integer id);
    public ErrorDetail getErrors(Customer customer);
}
