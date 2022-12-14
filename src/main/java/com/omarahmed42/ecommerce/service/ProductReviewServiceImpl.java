package com.omarahmed42.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductReviewNotFoundException;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.repository.ProductReviewRepository;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository productReviewRepository;

    @Autowired
    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    @Transactional
    @Override
    public void addProductReview(ProductReview productReview) {
        productReviewRepository.save(productReview);
    }

    @Transactional
    @Override
    public void deleteProductReview(byte[] id) {
        productReviewRepository
                .findById(id)
                .ifPresentOrElse(productReviewRepository::delete,
                        () -> {
                            throw new ProductReviewNotFoundException("Product review not found");
                        });
    }

    @Transactional
    @Override
    public void updateProductReview(ProductReview productReview) {
        productReviewRepository
                .findById(productReview.getId())
                .ifPresentOrElse(present -> productReviewRepository.save(productReview),
                        () -> {
                            throw new ProductReviewNotFoundException("Product review not found");
                        });
    }

    @Transactional
    @Override
    public byte[] findCustomerIdById(byte[] productReviewId) {
        return productReviewRepository.findCustomerIdById(productReviewId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for the given product review id"));
    }

}
