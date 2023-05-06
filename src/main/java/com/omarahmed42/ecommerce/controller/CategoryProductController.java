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
import com.omarahmed42.ecommerce.service.CategoryProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Tags(value = { @Tag(name = "Category Product") })
public class CategoryProductController {
        private final CategoryProductService categoryProductService;

        @PostMapping(value = "/products/{product-id}/categories", consumes = "application/json")
        @Operation(summary = "Assigns an already existing category to a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Created"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "422", description = "Categories ids are missing"),
                        @ApiResponse(responseCode = "422", description = "Product id is missing")
        })
        public ResponseEntity<Void> addCategoryProduct(
                        @PathVariable("product-id") @Parameter(name = "productId", description = "Product id") UUID productId,
                        @Parameter(description = "A Set of ids for categories") @RequestBody Set<Integer> categoriesIds) {
                categoryProductService.addAllCategoryProduct(categoriesIds, productId);
                return ResponseEntity.status(201).build();
        }

        @DeleteMapping(value = "/categories/{category-id}/products/{product-id}", consumes = "*/*")
        @Operation(summary = "Remove an already existing category from a product (unassociate a category from a product)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "No content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
        })
        public ResponseEntity<Void> deleteCategoryProduct(
                        @Parameter(name = "product-id", description = "Product id") @PathVariable(name = "product-id") UUID productId,
                        @Parameter(name = "category-id", description = "Category id") @PathVariable(name = "category-id") Integer categoryId) {
                categoryProductService.deleteCategoryFromProduct(categoryId, productId);
                return ResponseEntity.noContent().build();
        }

        @GetMapping(value = "/products/{product-id}/categories", consumes = "*/*")
        @Operation(summary = "Retrieves categories assigned to a product")
        @ApiResponse(responseCode = "200", description = "OK")
        public ResponseEntity<List<CategoryDTO>> getCategoriesOfProduct(
                        @Parameter(name = "product-id", description = "Product id") @PathVariable(name = "product-id") UUID productId) {
                List<CategoryDTO> categories = categoryProductService
                                .getCategoriesOfProduct(productId);
                return ResponseEntity.ok(categories);
        }
}
