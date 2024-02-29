package com.microservice.user.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.error.ErrorDetails;
import com.microservice.user.service.CustomerService;
import com.microservice.user.utils.MessagePropertyUtils;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MessagePropertyUtils messageUtils;

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

        // TODO
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

    @Override
    public ErrorDetails getErrors(Customer customer) {
        ErrorDetails errorDetails = new ErrorDetails();

        if(customer.getConstructionList() == null || customer.getConstructionList().size() == 0) {
            errorDetails.getDetails().put("construction", messageUtils.getMessage("missing_construction_error"));
        }

        if(customer.getUser() == null || customer.getUser().getUsername() == null || customer.getUser().getPassword() == null) {
            errorDetails.getDetails().put("user", messageUtils.getMessage("missing_user_error"));
        }

        if(customer.getConstructionList() != null) {
            Boolean haveConstructionType = customer.getConstructionList()
                .stream()
                .allMatch(construction -> construction.getConstructionType() != null && construction.getConstructionType().getType() != null)
            ;

            if(!haveConstructionType) {
                errorDetails.getDetails().put("constructionType", messageUtils.getMessage("missing_construction_type_error"));
            }
        }

        return errorDetails;
    }

}
