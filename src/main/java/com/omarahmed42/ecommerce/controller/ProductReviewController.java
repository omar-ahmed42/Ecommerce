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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@Tags(@Tag(name = "Product Review"))
@RequiredArgsConstructor
public class ProductReviewController {

        private final ProductReviewService productReviewService;

        @PostMapping(value = "/products/{product-id}/reviews")
        @Operation(summary = "Creates a product review")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Created"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "422", description = "Product review rating is missing"),
                        @ApiResponse(responseCode = "422", description = "Product review rating must be between 0 and 5")
        })
        public ResponseEntity<Void> addProductReview(@PathVariable("product-id") UUID productId,
                        @RequestBody ProductReviewRequest productReviewRequest) {
                productReviewService.addProductReview(productId, productReviewRequest);
                return ResponseEntity.status(201).build();
        }

        @DeleteMapping("/products/{product-id}/reviews/{review-id}")
        @Operation(summary = "Deletes a product review")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to delete a product review"),
                        @ApiResponse(responseCode = "404", description = "Product review not found")
        })
        public ResponseEntity<Void> deleteProductReview(@PathVariable("product-id") UUID productId,
                        @PathVariable("review-id") UUID reviewId) {
                productReviewService.deleteProductReview(reviewId);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(value = "/products/{product-id}/reviews/{review-id}")
        @Operation(summary = "Updates a product review")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to update a product review"),
                        @ApiResponse(responseCode = "404", description = "Product review not found"),
                        @ApiResponse(responseCode = "422", description = "Product review rating is missing"),
                        @ApiResponse(responseCode = "422", description = "Product review rating must be between 0 and 5")
        })
        public ResponseEntity<Void> updateProductReview(@PathVariable("product-id") UUID productId,
                        @PathVariable("review-id") UUID reviewId,
                        @RequestBody ProductReviewRequest productReviewRequest) {
                productReviewService.updateProductReview(reviewId, productReviewRequest);
                return ResponseEntity.noContent().build();
        }

}
