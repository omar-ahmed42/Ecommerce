package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{


    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public void addCustomer(Customer customer) {
        if (userRepository.existsById(customer.getUserId())){
            throw new UserNotFoundException("Cannot add a new customer for a non-existent user");
        }

        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public void deleteCustomer(byte[] id) {
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

        if (customer.getUserId() != customerRetrieved.get().getUserId() && !userRepository.existsById(customer.getUserId())){
                throw new UserNotFoundException("Cannot set a customer's userId to an non-existent one");
        }

        customerRepository.save(customer);
    }
}
