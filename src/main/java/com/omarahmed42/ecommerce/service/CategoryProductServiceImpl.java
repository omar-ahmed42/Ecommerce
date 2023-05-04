package com.omarahmed42.ecommerce.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.exception.CategoryNotFoundException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.CategoryRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class CategoryProductServiceImpl implements CategoryProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addCategoryToProduct(Integer categoryId, UUID productId) {
        if (categoryId == null)
            throw new MissingFieldException("Category id is missing");

        if (productId == null)
            throw new MissingFieldException("Product id is missing");

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        product.getCategories().add(categoryRepository.getReferenceById(categoryId));
        productRepository.save(product);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addAllCategoryProduct(Set<Integer> categoriesIds, UUID productId) {
        if (categoriesIds == null || categoriesIds.isEmpty())
            throw new MissingFieldException("Categories ids are missing");

        if (productId == null)
            throw new MissingFieldException("Product id is missing");

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.getCategories().addAll(getReferencesByIds(categoriesIds));
        productRepository.save(product);
    }

    private List<Category> getReferencesByIds(Collection<Integer> categoriesIds) {
        if (categoriesIds == null || categoriesIds.isEmpty())
            return Collections.emptyList();

        return categoriesIds.stream().filter(Objects::nonNull).map(categoryRepository::getReferenceById).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategoryFromProduct(Integer categoryId, UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        product.getCategories().remove(category);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesOfProduct(UUID productId) {
        List<Category> categories = productRepository.findCategoriesById(productId);
        return modelMapper.map(categories, new TypeToken<List<CategoryDTO>>() {
        }.getType());
    }
}
