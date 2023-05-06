package com.omarahmed42.ecommerce.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.ReviewCommentRequest;
import com.omarahmed42.ecommerce.DTO.ReviewCommentResponse;
import com.omarahmed42.ecommerce.service.ReviewCommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Tags(@Tag(name = "Review Comment"))
public class ReviewCommentController {

        private final ReviewCommentService reviewCommentService;

        @PostMapping(value = "/reviews/{review-id}/comments")
        @Operation(summary = "Adds a comment to a product review")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Created"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to add a review comment"),
                        @ApiResponse(responseCode = "404", description = "Product review not found"),
                        @ApiResponse(responseCode = "422", description = "Review title is missing"),
                        @ApiResponse(responseCode = "422", description = "Review content is missing"),
        })
        public ResponseEntity<Void> addReviewComment(
                        @Parameter(name = "review-id", description = "Product review id") @PathVariable("review-id") UUID reviewId,
                        @RequestBody ReviewCommentRequest reviewCommentRequest) {
                UUID commentId = reviewCommentService.addReviewComment(reviewId, reviewCommentRequest);
                return ResponseEntity.created(URI.create("/reviews/" + reviewId + "/comments/" + commentId)).build();
        }

        @DeleteMapping(value = "/reviews/{review-id}/comments/{comment-id}", consumes = "*/*")
        @Operation(summary = "Deletes a review comment")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to delete a review comment"),
                        @ApiResponse(responseCode = "404", description = "Review comment not found")
        })
        public ResponseEntity<Void> deleteReviewComment(
                        @Parameter(name = "review-id", description = "Product review id") @PathVariable("review-id") UUID reviewId,
                        @PathVariable("comment-id") UUID commentId) {
                reviewCommentService.deleteReviewComment(commentId);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(value = "/reviews/{review-id}/comments/{comment-id}")
        @Operation(summary = "Updates a review comment")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to update a review comment"),
                        @ApiResponse(responseCode = "404", description = "Review comment not found"),
                        @ApiResponse(responseCode = "422", description = "Review title is missing"),
                        @ApiResponse(responseCode = "422", description = "Review content is missing"),
        })
        public ResponseEntity<Void> updateReviewComment(
                        @Parameter(name = "review-id", description = "Product review id") @PathVariable("review-id") UUID reviewId,
                        @PathVariable("comment-id") UUID commentId,
                        @RequestBody ReviewCommentRequest reviewCommentRequest) {
                reviewCommentService.updateReviewComment(commentId, reviewCommentRequest);
                return ResponseEntity.noContent().build();
        }

        @GetMapping(value = "/reviews/{review-id}/comments/{comment-id}", consumes = "*/*")
        @Operation(summary = "Updates a review comment")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "404", description = "Review comment not found"),
        })
        public ResponseEntity<ReviewCommentResponse> getReviewComment(
                        @Parameter(name = "review-id", description = "Product review id") @PathVariable("review-id") UUID reviewId,
                        @PathVariable("comment-id") UUID commentId) {
                return ResponseEntity.ok(reviewCommentService.getReviewComment(commentId));
        }
}
