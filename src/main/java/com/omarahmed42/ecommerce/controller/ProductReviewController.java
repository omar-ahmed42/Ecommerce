package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.ProductReviewRequest;
import com.omarahmed42.ecommerce.service.ProductReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping(value = "/products/{product-id}/reviews")
    public ResponseEntity<Void> addProductReview(@PathVariable("product-id") UUID productId,
            @RequestBody ProductReviewRequest productReviewRequest) {
        productReviewService.addProductReview(productId, productReviewRequest);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/products/{product-id}/reviews/{review-id}")
    public ResponseEntity<Void> deleteProductReview(@PathVariable("product-id") UUID productId,
            @PathVariable("review-id") UUID reviewId) {
        productReviewService.deleteProductReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/products/{product-id}/reviews/{review-id}")
    public ResponseEntity<Void> updateProductReview(@PathVariable("product-id") UUID productId,
            @PathVariable("review-id") UUID reviewId,
            @RequestBody ProductReviewRequest productReviewRequest) {
        productReviewService.updateProductReview(reviewId, productReviewRequest);
        return ResponseEntity.noContent().build();
    }

}
