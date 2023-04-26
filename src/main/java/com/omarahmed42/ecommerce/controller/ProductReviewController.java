package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.ProductReviewDTO;
import com.omarahmed42.ecommerce.exception.ProductReviewNotFoundException;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.service.ProductReviewService;

@RestController
@RequestMapping("/v1")
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @PostMapping(value = "/customer/{customerId}/products/{productId}/reviews", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> addNewProductReview(@PathVariable("productId") UUID productId,
            @RequestBody ProductReviewDTO productReviewDTO,
            @PathVariable(name = "customerId") UUID customerId) {
        try {
            // TODO: Allow only users who purchased product X to review it
            Integer rating = productReviewDTO.getRating();
            if (!isValidRating(rating)){
                return ResponseEntity.unprocessableEntity().build();
            }

            ProductReview productReview = new ProductReview();
            productReview.setCustomerId(customerId);
            productReview.setProductId(productId);
            productReview.setRating(rating);
            productReviewService.addProductReview(productReview);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isValidRating(Integer rating){
        return (rating != null) && (rating <= 5) && (rating >= 1);
    }

    @DeleteMapping("/products/{productId}/reviews/{reviewId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> deleteProductReview(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") UUID reviewId) {
        try {
            productReviewService.deleteProductReview(reviewId);
            return ResponseEntity.noContent().build();
        } catch (ProductReviewNotFoundException productReviewNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/products/{productId}/reviews/{reviewId}", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> updateProductReview(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") UUID reviewId,
            @RequestBody ProductReviewDTO productReviewDTO) {
        try {
            Integer rating = productReviewDTO.getRating();
            if (!isValidRating(rating)){
                return ResponseEntity.unprocessableEntity().build();
            }
            ProductReview productReview = new ProductReview();
            productReview.setId(reviewId);
            productReview.setProductId(productId);
            productReview.setRating(rating);
            productReviewService.updateProductReview(productReview);
            return ResponseEntity.noContent().build();
        } catch (ProductReviewNotFoundException productReviewNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
