package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.ProductReviewRequest;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.ProductReviewNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.ProductReviewRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void addProductReview(UUID productId, ProductReviewRequest productReviewRequest) {
        validateProductReview(productReviewRequest);
        ProductReview productReview = new ProductReview();
        productReview.setProduct(productRepository.getReferenceById(productId));
        productReview.setCustomer(customerRepository.getReferenceById(UserDetailsUtils.getAuthenticatedUser().getId()));
        productReview.setRating(productReviewRequest.getRating());
        productReviewRepository.save(productReview);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('CUSTOMER')")
    public void deleteProductReview(UUID id) {
        ProductReview productReview = productReviewRepository
                .findById(id).orElseThrow(ProductReviewNotFoundException::new);
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!UserDetailsUtils.hasRole(Role.ADMIN) && !isOwner(productReview, authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "delete", "a product review");
        productReviewRepository.delete(productReview);
    }

    private boolean isOwner(ProductReview productReview, UUID userId) {
        return productReview.getCustomer().getId().equals(userId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void updateProductReview(UUID id, ProductReviewRequest productReviewRequest) {
        validateProductReview(productReviewRequest);
        ProductReview productReview = productReviewRepository
                .findById(id).orElseThrow(ProductReviewNotFoundException::new);
        UUID authenticatedUserId = UserDetailsUtils.getAuthenticatedUser().getId();
        if (!productReview.getCustomer().getId().equals(authenticatedUserId))
            throw new UnauthorizedAccessException(authenticatedUserId.toString(), "update",
                    "a product review");
        productReview.setRating(productReviewRequest.getRating());
        productReviewRepository.save(productReview);
    }

    private void validateProductReview(ProductReviewRequest productReviewRequest) {
        if (productReviewRequest.getRating() == null)
            throw new MissingFieldException("Product review rating is missing");
        if (productReviewRequest.getRating() < MIN_RATING || productReviewRequest.getRating() > MAX_RATING)
            throw new InvalidInputException(
                    "Product review rating must be between %d and %d".formatted(MIN_RATING, MAX_RATING));
    }

}
