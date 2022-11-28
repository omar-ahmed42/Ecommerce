package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    void deleteProduct(byte[] id);
    void updateProduct(Product product);
    Product getProductById(byte[] id);
    void updateProducts(List<Product> products);
}
