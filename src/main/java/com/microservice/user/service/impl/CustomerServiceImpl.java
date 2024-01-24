package com.microservice.user.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.microservice.user.domain.Customer;
import com.microservice.user.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    @Override
    public List<Customer> getAllCustomers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCustomers'");
    }

    @Override
    public Customer getCustomerById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerById'");
    }

    @Override
    public Customer getCustomerByParam(String cuit, String businessName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerByParam'");
    }

    @Override
    public Customer createCustomer(Customer customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCustomer'");
    }

    @Override
    public Customer editCustomer(Customer customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editCustomer'");
    }

    @Override
    public Boolean validateCustomer(Customer customer) {

        Boolean hasConstruction = customer.getConstructionList().size() > 0;
        Boolean hasUser = customer.getUser() != null;
        Boolean hasPassword = customer.getUser().getPassword() != null;

        return hasConstruction && hasUser && hasPassword;
    }

    @Override
    public Customer enableCustomer(Customer customer) {
        
        String riskBCRA = "Normal";
        customer.setOnlineEnabled(riskBCRA.equals("Normal") || riskBCRA.equals("Low risk"));

        return customer;
    }

    @Override
    public Customer disableCustomer(Customer customer) {
        
        customer.setDischargeDate(LocalDate.now());

        return customer;
    }
    
}
