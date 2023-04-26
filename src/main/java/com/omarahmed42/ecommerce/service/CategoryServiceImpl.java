package com.omarahmed42.ecommerce.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.exception.CategoryAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository
                .findByName(category.getName())
                .ifPresentOrElse(presentCategory -> new CategoryAlreadyExistsException("Category already exists"),
                        () -> categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository
                .findById(id)
                .ifPresentOrElse(presentCategory -> categoryRepository.deleteById(presentCategory.getId()),
                        CategoryNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository
                .findById(category.getId())
                .ifPresentOrElse(presentCategory -> categoryRepository.save(category),
                        CategoryNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        return modelMapper.map(category, CategoryDTO.class);
    }
}
