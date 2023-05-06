package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.PageResponse;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.enums.ProductSort;

public interface WishlistService {
    void addWishlist(UUID productId);

    void deleteWishlist(UUID productId);

    ProductResponse getWishlist(UUID customerId, UUID productId);

    PageResponse<ProductResponse> getWishlistItems(UUID customerId, Integer page, Integer size, ProductSort sortOrder);
}
