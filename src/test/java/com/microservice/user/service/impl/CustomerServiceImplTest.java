package com.microservice.user.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.user.dao.CustomerRepository;
import com.microservice.user.domain.Construction;
import com.microservice.user.domain.Customer;
import com.microservice.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
            "PedritoConstructions",
            "20-43535431-4",
            "pedrito@gmail.com",
            false,
            500d
        );
        customer.setUser(new User("pedrito123", "1234"));

        Construction construction = new Construction(
            "Test construction",
            34.412f,
            40.634f,
            "Dir - Test 324",
            50
        );

        customer.setConstructionList(List.of(construction));
    }

    @Test
    void testCreateCustomer() {
        //given
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customerResult = invocation.getArgument(0);
            customerResult.setId(1);
            customerResult.getUser().setId(1);
            customerResult.getConstructionList().get(0).setId(1);
            return customerResult;
        });

        //when
        Customer newCustomer = customerService.createCustomer(customer);

        //then
        assertThat(newCustomer.getId()).isEqualTo(1);
        assertThat(newCustomer.getConstructionList().size()).isEqualTo(1);
        assertThat(newCustomer.getConstructionList().get(0).getCustomer().getId()).isEqualTo(newCustomer.getId());
    }

    @Test
    void testDisableCustomer() {
        //given
        int idCustomer = 1;
        customer.setId(idCustomer);
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //when
        Customer customerDisabled = customerService.disableCustomer(idCustomer);

        //then
        assertThat(customerDisabled.getId()).isEqualTo(idCustomer);
        assertThat(customerDisabled.getDischargeDate()).isNotNull();
    }

    @Test
    void testValidateCustomer() {
        // when
        Boolean validation = customerService.validateCustomer(customer);

        assertTrue(validation);
    }
}
