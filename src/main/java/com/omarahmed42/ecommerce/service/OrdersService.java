package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.ProductItem;

public interface OrdersService {
    void addOrder(Orders order);

    void deleteOrder(UUID id);

    void updateOrder(Orders order);

    Orders addNewOrders(UUID customerId, ProductItem[] productItem, BillingAddress billingAddress);

    Orders getOrder(UUID orderId);
}
