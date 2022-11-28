package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.ProductItem;

public interface OrdersService {
    void addOrder(Orders order);

    void deleteOrder(byte[] id);

    void updateOrder(Orders order);

    Orders addNewOrders(byte[] customerId, ProductItem[] productItem, BillingAddress billingAddress);

    Orders getOrder(byte[] orderId);
}
