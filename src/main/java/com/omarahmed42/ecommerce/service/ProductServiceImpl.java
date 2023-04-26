package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        productRepository
                .findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ProductNotFoundException("Product not found");
                });
    }

    @Transactional
    @Override
    public void updateProduct(Product product) {
        productRepository
                .findById(product.getId()) // A custom findById could be implemented to project only the "id" of the entity
                .ifPresentOrElse(present -> {
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(product, present);
                    productRepository.save(present);
                },
                        () -> {
                            throw new ProductNotFoundException("Product not found");
                        });
    }

    @Transactional
    @Override
    public Product getProductById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    @Override
    public void updateProducts(List<Product> products) {
        productRepository
                .saveAll(products);
    }
}
