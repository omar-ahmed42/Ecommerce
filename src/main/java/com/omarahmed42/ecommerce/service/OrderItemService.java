package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.OrderItem;

public interface OrderItemService {
    void addOrderItem(OrderItem productItem);
    void deleteOrderItem(OrderItem productItem);
    void updateOrderItem(OrderItem productItem);
    OrderItem getOrderItem(UUID productItemId);
}
