package com.microservice.user.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Customer getCustomerByParam(String cuit, String businessName) {
        return customerRepository.findByCuitOrBusinessName(cuit, businessName).get();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        List<Construction> constructionList = customer.getConstructionList();

        if (constructionList != null) {
            for (Construction construction : constructionList) {
                construction.setCustomer(customer);
            }
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer editCustomer(Customer customer) {
        return customerRepository.save(customer);
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
    public Customer disableCustomer(Integer id) {

        Customer customer = customerRepository.findById(id).orElse(null);
        customer.setDischargeDate(LocalDate.now());

        return customerRepository.save(customer);
    }

}
