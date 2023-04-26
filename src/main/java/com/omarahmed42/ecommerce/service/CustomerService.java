package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.Customer;

public interface CustomerService {
    public void addCustomer(Customer customer);
    public void deleteCustomer(UUID id);
    public void updateCustomer(Customer customer);
}
