package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;

public interface CategoryProductService {
    void addCategoryToProduct(Integer categoryId, UUID productId);

    void addAllCategoryProduct(Set<Integer> categoriesIds, UUID productId);

    void deleteCategoryFromProduct(CategoryProductPK categoryProductPK);

    List<CategoryDTO> getCategoriesOfProduct(UUID productId);
}
