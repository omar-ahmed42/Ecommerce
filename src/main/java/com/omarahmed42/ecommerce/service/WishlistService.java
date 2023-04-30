package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.ProductResponse;

public interface WishlistService {
    void addWishlist(UUID productId);

    void deleteWishlist(UUID productId);

    ProductResponse getWishlist(UUID customerId, UUID productId);
}
