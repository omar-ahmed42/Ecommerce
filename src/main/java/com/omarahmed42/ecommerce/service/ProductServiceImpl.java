package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.ADMIN.toString()) || (principal.userId == #vendorId)")
    public ProductResponse addProduct(UUID vendorId, ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        productRepository
                .findById(id)
                .ifPresentOrElse(productRepository::delete, ProductNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateProduct(UUID id, ProductRequest productRequest) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
        product = modelMapper.map(productRequest, Product.class);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Transactional
    @Override
    public void updateProducts(List<Product> products) {
        productRepository
                .saveAll(products);
    }
}
