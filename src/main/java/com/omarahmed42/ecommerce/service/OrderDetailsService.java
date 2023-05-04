package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.BillingAddressDTO;
import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.model.OrderDetails;

public interface OrderDetailsService {
    void deleteOrderDetails(UUID id);

    void updateOrderDetailsPartially(UUID id, OrderDetailsDTO orderDetailsDTO);

    void updateOrderDetails(OrderDetails order);

    OrderDetails getOrderDetails(UUID orderId);

    OrderDetails addOrderDetails(UUID userId, CartItemDTO[] cartItems, BillingAddressDTO billingAddress);
}
