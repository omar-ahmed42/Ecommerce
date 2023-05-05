package com.omarahmed42.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@Tags(value = { @Tag(name = "Category") })
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/categories")
    @Operation(summary = "Adds a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "409", description = "Category already exists")
    })
    public ResponseEntity<Void> addCategory(@RequestBody @Parameter(name = "category") CategoryDTO category) {
        categoryService.addCategory(category);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Deletes a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("id") @Parameter(name = "id", description = "Category id") Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Updates a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> updateCategory(
            @PathVariable("id") @Parameter(name = "id", description = "Category id") Integer id,
            @RequestBody @Parameter(name = "category") CategoryDTO category) {
        categoryService.updateCategory(id, category);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "Retrieves a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryDTO> getCategory(
            @PathVariable("id") @Parameter(name = "id", description = "Category id") Integer id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
}
