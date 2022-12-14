package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.ProductItem;

public interface ProductItemService {
    void addProductItem(ProductItem productItem);
    void deleteProductItem(ProductItem productItem);
    void updateProductItem(ProductItem productItem);
    ProductItem getProductItem(byte[] productItemId);
}
