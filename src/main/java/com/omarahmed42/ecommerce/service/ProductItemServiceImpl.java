package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.ProductItemNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.ProductItem;
import com.omarahmed42.ecommerce.repository.ProductItemRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductItemServiceImpl implements ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductItemServiceImpl(ProductItemRepository productItemRepository, ProductRepository productRepository) {
        this.productItemRepository = productItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void addProductItem(ProductItem productItem) {
        productRepository
                .findById(productItem.getProductId())
                .ifPresentOrElse(
                        presentProduct -> productItemRepository.save(productItem),
                        () -> {
                            throw new ProductNotFoundException("Product not found");
                        });
    }

    @Transactional
    @Override
    public void deleteProductItem(ProductItem productItem) {
        productItemRepository
                .findById(productItem.getId())
                .ifPresentOrElse(
                        productItemRepository::delete,
                        () -> {
                            throw new ProductItemNotFoundException("Product item not found");
                        }
                );
    }

    @Transactional
    @Override
    public void updateProductItem(ProductItem productItem) {
        Optional<ProductItem> productItemById = productItemRepository.findById(productItem.getId());

        if (productItemById.isEmpty()) {
            throw new ProductItemNotFoundException("Product item not found");
        } else if (productItemById.get().getProductId() != productItem.getProductId()
                && productRepository.findById(productItem.getProductId()).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        productItemRepository.save(productItem);
    }

    @Transactional
    @Override
    public ProductItem getProductItem(byte[] productItemId) {
        return productItemRepository
                .findById(productItemId)
                .orElseThrow(() -> new ProductItemNotFoundException("Product item not found"));
    }
}
