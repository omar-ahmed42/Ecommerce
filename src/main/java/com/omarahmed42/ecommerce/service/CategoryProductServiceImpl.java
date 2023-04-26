package com.omarahmed42.ecommerce.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.exception.CategoryProductNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.CategoryProductRepository;
import com.omarahmed42.ecommerce.repository.CategoryRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class CategoryProductServiceImpl implements CategoryProductService {

    private final CategoryProductRepository categoryProductRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryProductServiceImpl(CategoryProductRepository categoryProductRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryProductRepository = categoryProductRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void addCategoryToProduct(CategoryProduct categoryProduct) {
        if (categoryRepository.findById(categoryProduct.getCategoryId()).isEmpty()) {
            throw new CategoryNotFoundException("Category not found");
        } else if (productRepository.findById(categoryProduct.getProductId()).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        categoryProductRepository.save(categoryProduct);
    }

    @Transactional
    @Override
    public void addAllCategoryProduct(List<CategoryProduct> categoryProducts) {
        categoryProductRepository.saveAll(categoryProducts);
    }

    @Transactional
    @Override
    public void deleteCategoryFromProduct(CategoryProductPK categoryProductPK) {
        categoryProductRepository.findById(categoryProductPK)
                .ifPresentOrElse(categoryProductRepository::delete,
                        () -> {
                            throw new CategoryProductNotFoundException("Category Product not found");
                        });
    }

    @Transactional
    @Override
    public void updateCategoryProduct(CategoryProduct categoryProduct) {
        CategoryProductPK categoryProductPK = new CategoryProductPK(categoryProduct.getCategoryId(), categoryProduct.getProductId());
        categoryProductRepository.findById(categoryProductPK)
                .ifPresentOrElse(present -> categoryProductRepository.save(categoryProduct),
                        () -> {
                            throw new CategoryProductNotFoundException("Category Product not found");
                        });
    }

    @Transactional
    @Override
    public List<Category> getCategoriesOfProduct(UUID productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Collection<CategoryProduct> categoryProductsById = product.getCategoryProductsById();
        List<Category> categories = new ArrayList<>(categoryProductsById.size());
        for (CategoryProduct categoryProduct : categoryProductsById) {
            categories.add(categoryProduct.getCategoryByCategoryId());
        }
        return categories;
    }
}
