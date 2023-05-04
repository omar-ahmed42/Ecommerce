package com.omarahmed42.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.CustomerOrderAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CustomerOrderNotFoundException;
import com.omarahmed42.ecommerce.exception.OrderNotFoundException;
import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.CustomerOrdersPK;
import com.omarahmed42.ecommerce.repository.CustomerOrdersRepository;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.OrderDetailsRepository;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrdersRepository customerOrdersRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailsRepository orderRepository;

    public CustomerOrderServiceImpl(CustomerOrdersRepository customerOrdersRepository, CustomerRepository customerRepository, OrderDetailsRepository orderRepository) {
        this.customerOrdersRepository = customerOrdersRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void addOrderToCustomer(CustomerOrders customerOrder) {
        customerOrdersRepository
                .findById(new CustomerOrdersPK(customerOrder.getCustomerId(), customerOrder.getOrderId()))
                .ifPresent(present -> {throw new CustomerOrderAlreadyExistsException("Customer order already exists");});

        if (customerRepository.findById(customerOrder.getCustomerId()).isEmpty()){
            throw new CustomerNotFoundException("Customer not found");
        } else if (orderRepository.findById(customerOrder.getOrderId()).isEmpty()){
            throw new OrderNotFoundException("Order not found");
        }

        customerOrdersRepository.save(customerOrder);
    }

    @Transactional
    @Override
    public void addOrdersToCustomer(List<CustomerOrders> customerOrders) {
        customerOrdersRepository.saveAll(customerOrders);
    }

    @Transactional
    @Override
    public void deleteCustomerOrder(CustomerOrders customerOrder) {
        customerOrdersRepository
                .findById(new CustomerOrdersPK(customerOrder.getCustomerId(), customerOrder.getOrderId()))
                .ifPresentOrElse(
                        customerOrdersRepository::delete,
                        () -> {throw new CustomerOrderNotFoundException("Customer order not found");}
                );
    }

    @Transactional
    @Override
    public void deleteCustomerOrders(List<CustomerOrders> customerOrders) {
        customerOrdersRepository.deleteAll(customerOrders);
    }

    @Transactional
    @Override
    public void updateCustomerOrder(CustomerOrders customerOrder) {
        // TODO: implement it
    }
}
