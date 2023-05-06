package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.PageResponse;
import com.omarahmed42.ecommerce.DTO.ProductFilter;
import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.enums.ProductSort;

public interface ProductService {
    ProductResponse addProduct(UUID vendorId, ProductRequest productRequest);

    void deleteProduct(UUID id);

    void updateProduct(UUID id, ProductRequest productRequest);

    ProductResponse getProduct(UUID id);

    PageResponse<ProductResponse> getProducts(Integer page, Integer size, ProductSort sortOrder, ProductFilter filter);
}
