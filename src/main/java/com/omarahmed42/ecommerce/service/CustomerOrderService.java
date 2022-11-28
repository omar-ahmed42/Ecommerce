package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.CustomerOrders;

import java.util.List;

public interface CustomerOrderService {
    void addOrderToCustomer(CustomerOrders customerOrder);
    void addOrdersToCustomer(List<CustomerOrders> customerOrders);
    void deleteCustomerOrder(CustomerOrders customerOrder);
    void deleteCustomerOrders(List<CustomerOrders> customerOrders);
    void updateCustomerOrder(CustomerOrders customerOrder);
}
