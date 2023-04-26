package com.omarahmed42.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.DTO.CategoryProductCreationDTO;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.exception.CategoryProductNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;
import com.omarahmed42.ecommerce.service.CategoryProductService;

@RestController
@RequestMapping("/v1")
public class CategoryProductController {
    private final CategoryProductService categoryProductService;
    private static ModelMapper modelMapper = new ModelMapper();

    public CategoryProductController(CategoryProductService categoryProductService) {
        this.categoryProductService = categoryProductService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/products/categories", consumes = "application/json")
    public ResponseEntity<String> addNewCategoryProduct(
            @RequestBody List<CategoryProductCreationDTO> categoryProductCreationDTOS) {
        try {
            List<CategoryProduct> categoryProducts = createCategoryProductCollectionFromDTO(
                    categoryProductCreationDTOS);
            categoryProductService.addAllCategoryProduct(categoryProducts);
            return ResponseEntity.status(201).build();
        } catch (CategoryNotFoundException | ProductNotFoundException | CategoryProductNotFoundException
                | EntityNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<CategoryProduct> createCategoryProductCollectionFromDTO(
            List<CategoryProductCreationDTO> creationDTOS) {
        List<CategoryProduct> categoryProducts = new ArrayList<>(creationDTOS.size());
        for (int i = 0; i < creationDTOS.size(); i++) {
            categoryProducts
                    .add(
                            new CategoryProduct(creationDTOS.get(i).getCategoryId(),
                                    creationDTOS.get(i).getProductId()));
        }
        return categoryProducts;
    }

    @DeleteMapping("/products/{product-id}/categories/{category-id}")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> deleteCategoryProduct(@PathVariable(name = "product-id") UUID productId,
            @PathVariable(name = "category-id") int categoryId) {
        try {
            categoryProductService.deleteCategoryFromProduct(
                    new CategoryProductPK(categoryId, productId));
            return ResponseEntity.noContent().build();
        } catch (CategoryProductNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/products/{product-id}/categories", produces = "application/json")
    public ResponseEntity<String> getCategoriesOfProduct(@PathVariable(name = "product-id") UUID productId) {
        try {
            List<Category> categoriesOfProduct = categoryProductService
                    .getCategoriesOfProduct(productId);
            List<CategoryDTO> categories = categoriesOfProduct.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
            return ResponseEntity.ok()
                    .body(new Gson().toJson(categories));
        } catch (ProductNotFoundException | EntityNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
