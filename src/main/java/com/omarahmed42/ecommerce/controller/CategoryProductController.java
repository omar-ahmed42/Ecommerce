package com.omarahmed42.ecommerce.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.model.CategoryProductPK;
import com.omarahmed42.ecommerce.service.CategoryProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class CategoryProductController {
    private final CategoryProductService categoryProductService;

    @PostMapping(value = "/products/{product-id}/categories", consumes = "application/json")
    public ResponseEntity<Void> addCategoryProduct(@PathVariable("product-id") UUID productId,
            @RequestBody Set<Integer> categoriesIds) {
        categoryProductService.addAllCategoryProduct(categoriesIds, productId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/categories/{category-id}/products/{product-id}")
    public ResponseEntity<Void> deleteCategoryProduct(@PathVariable(name = "product-id") UUID productId,
            @PathVariable(name = "category-id") Integer categoryId) {
        categoryProductService.deleteCategoryFromProduct(
                new CategoryProductPK(categoryId, productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/products/{product-id}/categories")
    public ResponseEntity<List<CategoryDTO>> getCategoriesOfProduct(@PathVariable(name = "product-id") UUID productId) {
        List<CategoryDTO> categories = categoryProductService
                .getCategoriesOfProduct(productId);
        return ResponseEntity.ok(categories);
    }
}
