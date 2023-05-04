package com.omarahmed42.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.exception.CategoryAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.repository.CategoryRepository;
import com.omarahmed42.ecommerce.service.CategoryService;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void init() {
        category = new Category("Fashion");
        category = categoryRepository.saveAndFlush(category);
    }

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
        category = null;
    }

    @Test
    void addCategorySuccessfully() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Electronics");

        categoryService.addCategory(categoryDTO);

        Category expected = new Category(1, "Electronics");
        Category actual = categoryRepository.findByName("Electronics").get();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("categoryProducts", "id")
                .isEqualTo(expected);
    }

    @Test
    void addCategory_ThrowsCategoryAlreadyExistsException() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Fashion");
        org.junit.jupiter.api.Assertions.assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.addCategory(categoryDTO), "Category already exists");
    }

    @Test
    void deleteCategory_ThrowsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(99), "Category not found");
    }

    @Test
    void deleteCategorySuccessfully() {
        Category category = categoryRepository.save(new Category("Category to be deleted"));
        categoryService.deleteCategory(category.getId());
        assertTrue(categoryRepository.findById(category.getId()).isEmpty());
    }

    @Test
    void getCategory_ReturnsSuccessfully() {
        CategoryDTO expected = new CategoryDTO();
        expected.setId(category.getId());
        expected.setName("Fashion");

        CategoryDTO actual = categoryService.getCategory(category.getId());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
