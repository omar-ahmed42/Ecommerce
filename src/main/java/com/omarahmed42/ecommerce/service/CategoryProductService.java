package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;

public interface CategoryProductService {
    void addCategoryToProduct(CategoryProduct categoryProduct);

    void addAllCategoryProduct(List<CategoryProduct> categoryProducts);

    void deleteCategoryFromProduct(CategoryProductPK categoryProductPK);

    void updateCategoryProduct(CategoryProduct categoryProduct);
    List<Category> getCategoriesOfProduct(UUID productId);
}
