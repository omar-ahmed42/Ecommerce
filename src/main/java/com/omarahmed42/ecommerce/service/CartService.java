package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;

public interface CartService {
    void addCartItem(CartItemDTO cartItemDTO);
    void updateCartItem(UUID productId, CartItemDTO cartItemDTO);
    CartItemDTO getCartItem(UUID userId, UUID productId);

    void deleteCartItem(UUID productId);
}
