package com.microservice.user.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.jpa.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.dto.CustomerDTO;
import com.microservice.user.domain.dto.SaveCustomerRequest;
import com.microservice.user.error.ErrorDetail;
import com.microservice.user.service.ConstructionService;
import com.microservice.user.service.CustomerService;
import com.microservice.user.utils.MessagePropertyUtils;
import org.springframework.cache.annotation.Cacheable;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private MessagePropertyUtils messageUtils;

    @Override
    @Cacheable(value = "getAllCustomers")
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customer -> CustomerDTO.customerMapping(customer)).toList();
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
    @Transactional
    public Customer createCustomer(SaveCustomerRequest customer) {
        Customer newCustomer = new Customer();
        newCustomer.setBusinessName(customer.getBusinessName());
        newCustomer.setCuit(customer.getCuit());
        newCustomer.setEmail(customer.getEmail());

        List<Construction> constructionList = constructionService.constructionsDtoToConstructions(customer.getConstructionList(), newCustomer);
        newCustomer.setConstructionList(constructionList);

        return customerRepository.save(newCustomer);
    }

    @Override
    public Customer editCustomer(Customer customer) {
        return customerRepository.save(customer);
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

        Customer customer = customerRepository.findById(id).get();
        customer.setDischargeDate(LocalDate.now());

        return customerRepository.save(customer);
    }

    @Override
    public ErrorDetail getErrors(Customer customer) {
        ErrorDetail errorDetails = new ErrorDetail();

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
