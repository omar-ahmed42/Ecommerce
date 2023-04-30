package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.BillingAddressDTO;
import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.model.Orders;

public interface OrdersService {
    void addOrder(Orders order);

    void deleteOrder(UUID id);

    void updateOrderPartially(UUID id, OrderDetailsDTO orderDetailsDTO);

    void updateOrder(Orders order);

    Orders getOrder(UUID orderId);

    Orders addOrder(UUID userId, CartItemDTO[] cartItems, BillingAddressDTO billingAddress);
}
