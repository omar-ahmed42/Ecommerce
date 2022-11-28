package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Category;

public interface CategoryService {
    void addCategory(Category category);
    void deleteCategory(Category category);
    void updateCategory(Category category);
    Category getByName(String name);
}
