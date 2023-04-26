package com.omarahmed42.ecommerce.controller;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.exception.CategoryAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.service.CategoryService;

@RestController
@RequestMapping("/v1")
public class CategoryController {
    private final CategoryService categoryService;
    private static ModelMapper modelMapper = new ModelMapper();

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/category", consumes = "application/json")
    public ResponseEntity<Category> addNewCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = modelMapper.map(categoryDTO, Category.class);
            categoryService.addCategory(new Category(category.getName()));
            return ResponseEntity
                    .created(URI.create("/categories/" + categoryService.getByName(category.getName()).getId()))
                    .build();
        } catch (CategoryAlreadyExistsException categoryAlreadyExistsException) {
            return ResponseEntity.status(303).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") int id) {
        try {
            categoryService.deleteCategory(new Category(id));
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException categoryNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") int id, @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(new Category(id, categoryDTO.getName()));
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException categoryNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
