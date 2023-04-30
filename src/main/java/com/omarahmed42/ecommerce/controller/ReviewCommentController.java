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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class ReviewCommentController {

        private final ReviewCommentService reviewCommentService;

        @PostMapping(value = "/reviews/{review-id}/comments")
        public ResponseEntity<Void> addReviewComment(
                        @PathVariable("review-id") UUID reviewId,
                        @RequestBody ReviewCommentRequest reviewCommentRequest) {
                UUID commentId = reviewCommentService.addReviewComment(reviewId, reviewCommentRequest);
                return ResponseEntity.created(URI.create("/reviews/" + reviewId + "/comments/" + commentId)).build();
        }

        @DeleteMapping("/reviews/{review-id}/comments/{comment-id}")
        public ResponseEntity<Void> deleteReviewComment(@PathVariable("review-id") UUID reviewId,
                        @PathVariable("comment-id") UUID commentId) {
                reviewCommentService.deleteReviewComment(commentId);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(value = "/reviews/{review-id}/comments/{comment-id}")
        public ResponseEntity<Void> updateReviewComment(@PathVariable("product-id") UUID productId,
                        @PathVariable("review-id") UUID reviewId, @PathVariable("comment-id") UUID commentId,
                        @RequestBody ReviewCommentRequest reviewCommentRequest) {
                reviewCommentService.updateReviewComment(commentId, reviewCommentRequest);
                return ResponseEntity.noContent().build();
        }

        @GetMapping(value = "/reviews/{review-id}/comments/{comment-id}")
        public ResponseEntity<ReviewCommentResponse> getReviewComment(@PathVariable("review-id") UUID reviewId,
                        @PathVariable("comment-id") UUID commentId) {
                return ResponseEntity.ok(reviewCommentService.getReviewComment(commentId));
        }
}
