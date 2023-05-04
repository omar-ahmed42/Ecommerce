package com.omarahmed42.ecommerce.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.OrderItemNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.OrderItem;
import com.omarahmed42.ecommerce.repository.OrderItemRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository productItemRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository productItemRepository, ProductRepository productRepository) {
        this.productItemRepository = productItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void addOrderItem(OrderItem productItem) {
        productRepository
                .findById(productItem.getProduct().getId())
                .ifPresentOrElse(
                        presentProduct -> productItemRepository.save(productItem),
                        () -> {
                            throw new ProductNotFoundException("Product not found");
                        });
    }

    @Transactional
    @Override
    public void deleteOrderItem(OrderItem productItem) {
        productItemRepository
                .findById(productItem.getId())
                .ifPresentOrElse(
                        productItemRepository::delete,
                        () -> {
                            throw new OrderItemNotFoundException("Order item not found");
                        }
                );
    }

    @Transactional
    @Override
    public void updateOrderItem(OrderItem productItem) {
        Optional<OrderItem> productItemById = productItemRepository.findById(productItem.getId());

        if (productItemById.isEmpty()) {
            throw new OrderItemNotFoundException("Order item not found");
        } else if (productItemById.get().getProduct().getId() != productItem.getProduct().getId()
                && productRepository.findById(productItem.getProduct().getId()).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        productItemRepository.save(productItem);
    }

    @Transactional
    @Override
    public OrderItem getOrderItem(UUID productItemId) {
        return productItemRepository
                .findById(productItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));
    }
}
