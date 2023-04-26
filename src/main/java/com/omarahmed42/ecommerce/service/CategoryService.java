package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;

public interface CategoryService {
    void addCategory(CategoryDTO category);
    void deleteCategory(Integer id);
    void updateCategory(Integer id, CategoryDTO category);
    CategoryDTO getCategory(Integer id);
}
