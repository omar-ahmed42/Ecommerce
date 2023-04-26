package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import com.omarahmed42.ecommerce.model.Product;

public interface ProductService {
    Product addProduct(Product product);
    void deleteProduct(UUID id);
    void updateProduct(Product product);
    Product getProductById(UUID id);
    void updateProducts(List<Product> products);
}
