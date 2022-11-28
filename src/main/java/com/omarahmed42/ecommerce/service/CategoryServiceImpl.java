package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.CategoryAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public void addCategory(Category category) {
        categoryRepository
                .findByName(category.getName())
                .ifPresentOrElse((presentCategory) -> {throw new CategoryAlreadyExistsException("Category already exists");},
                        () -> categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategory(Category category) {
        categoryRepository
                .findById(category.getId())
                .ifPresentOrElse(presentCategory -> categoryRepository.deleteById(presentCategory.getId()),
                () -> {throw new CategoryNotFoundException("Category not found");});
    }

    @Transactional
    @Override
    public void updateCategory(Category category) {
        categoryRepository
                .findById(category.getId())
                .ifPresentOrElse((presentCategory) -> categoryRepository.save(category),
                () -> {throw new CategoryNotFoundException("Category not found");});
    }

    @Transactional
    @Override
    public Category getByName(String name){
        return categoryRepository
                .findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category Not found"));
    }
}
