package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.CategoryDTO;
import com.omarahmed42.ecommerce.exception.CategoryProductNotFoundException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.CategoryProduct;
import com.omarahmed42.ecommerce.model.CategoryProductPK;
import com.omarahmed42.ecommerce.repository.CategoryProductRepository;

@Service
public class CategoryProductServiceImpl implements CategoryProductService {

    private final CategoryProductRepository categoryProductRepository;
    private ModelMapper modelMapper;

    public CategoryProductServiceImpl(CategoryProductRepository categoryProductRepository) {
        this.categoryProductRepository = categoryProductRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @Secured("Role.ADMIN.toString()")
    public void addCategoryToProduct(Integer categoryId, UUID productId) {

        if (categoryId == null)
            throw new MissingFieldException("Category id is missing");

        if (productId == null)
            throw new MissingFieldException("Product id is missing");

        categoryProductRepository.save(new CategoryProduct(categoryId, productId));
    }

    @Override
    @Transactional
    @Secured("Role.ADMIN.toString()")
    public void addAllCategoryProduct(Set<Integer> categoriesIds, UUID productId) {
        if (categoriesIds == null || categoriesIds.isEmpty())
            throw new MissingFieldException("Categories ids are missing");

        if (productId == null)
            throw new MissingFieldException("Product id is missing");

        categoryProductRepository.saveAll(categoriesIds.stream()
                .map(categoryId -> new CategoryProduct(categoryId, productId)).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    @Secured("Role.ADMIN.toString()")
    public void deleteCategoryFromProduct(CategoryProductPK categoryProductPK) {
        categoryProductRepository.findById(categoryProductPK)
                .ifPresentOrElse(categoryProductRepository::delete,
                        () -> {
                            throw new CategoryProductNotFoundException("Category Product not found");
                        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesOfProduct(UUID productId) {
        List<Category> categories = categoryProductRepository.findCategoriesByProductId(productId);
        return modelMapper.map(categories, new TypeToken<List<CategoryDTO>>() {
        }.getType());
    }
}
