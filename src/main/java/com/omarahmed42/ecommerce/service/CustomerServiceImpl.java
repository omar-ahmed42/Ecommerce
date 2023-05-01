package com.omarahmed42.ecommerce.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;

@Service
public class CustomerServiceImpl implements CustomerService {


    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public void addCustomer(Customer customer) {
        if (userRepository.existsById(customer.getId())){
            throw new UserNotFoundException("Cannot add a new customer for a non-existent user");
        }

        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public void deleteCustomer(UUID id) {
        if (customerRepository.existsById(id)){
            throw new CustomerNotFoundException("Customer not found");
        }

        customerRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateCustomer(Customer customer) {
        Optional<Customer> customerRetrieved = customerRepository.findById(customer.getId());
        if (customerRetrieved.isEmpty()){
            throw new CustomerNotFoundException("Customer not found");
        }

        if (customer.getId() != customerRetrieved.get().getId() && !userRepository.existsById(customer.getId())){
                throw new UserNotFoundException("Cannot set a customer's userId to an non-existent one");
        }

        customerRepository.save(customer);
    }
}
