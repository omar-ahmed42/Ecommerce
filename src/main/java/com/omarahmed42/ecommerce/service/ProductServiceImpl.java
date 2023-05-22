package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.PageResponse;
import com.omarahmed42.ecommerce.DTO.ProductFilter;
import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.enums.ProductSort;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.specification.ProductSpecification;
import com.omarahmed42.ecommerce.util.PageUtils;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;
import com.omarahmed42.ecommerce.validation.ProductValidation;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || ((principal.user.id == #vendorId) && hasRole('VERIFIED_VENDOR'))")
    public ProductResponse addProduct(UUID vendorId, ProductRequest productRequest) {
        ProductValidation.validateProduct(productRequest);
        Product product = modelMapper.map(productRequest, Product.class);
        Vendor vendor = vendorRepository.getReferenceById(vendorId);
        product.setVendor(vendor);
        product = productRepository.save(product);

        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public void deleteProduct(UUID id) {
        Product product = productRepository
                .findById(id).orElseThrow(ProductNotFoundException::new);
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (UserDetailsUtils.hasRole(Role.ADMIN) || isProductOwner(product, authenticatedUser))
            productRepository.delete(product);
        else
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "delete", "a product");

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public void updateProduct(UUID id, ProductRequest productRequest) {
        ProductValidation.validateProduct(productRequest);
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);

        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!UserDetailsUtils.hasRole(Role.ADMIN) && !isProductOwner(product, authenticatedUser))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "update", "a product");

        product = modelMapper.map(productRequest, Product.class);
        productRepository.save(product);
    }

    private boolean isProductOwner(Product product, User authenticatedUser) {
        return product.getVendor().getId().equals(authenticatedUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(Integer page, Integer size, ProductSort sortOrder,
            ProductFilter filter) {
        Pageable pageable = PageRequest.of(page - 1, size, PageUtils.getSortOrder(sortOrder));
        Page<Product> products = productRepository.findAll(ProductSpecification.buildSpecification(filter), pageable);
        List<ProductResponse> productResponse = modelMapper.map(products.getContent(),
                new TypeToken<List<ProductResponse>>() {
                }.getType());
        return new PageResponse<>(products, productResponse);
    }

}
