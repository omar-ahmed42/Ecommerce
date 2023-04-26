package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.ReviewCommentDTO;
import com.omarahmed42.ecommerce.config.security.UserDetailsId;
import com.omarahmed42.ecommerce.exception.ReviewCommentNotFoundException;
import com.omarahmed42.ecommerce.model.ReviewComment;
import com.omarahmed42.ecommerce.service.ProductReviewService;
import com.omarahmed42.ecommerce.service.ReviewCommentService;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@RestController
@RequestMapping("/v1")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;
    private final ProductReviewService productReviewService;
    private static final Logger logger = LogManager.getLogger(ReviewCommentController.class);
    private final ModelMapper modelMapper = new ModelMapper();

    public ReviewCommentController(ReviewCommentService reviewCommentService,
            ProductReviewService productReviewService) {
        this.reviewCommentService = reviewCommentService;
        this.productReviewService = productReviewService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerId)")
    public ResponseEntity<String> addNewReviewComment(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") UUID reviewId,
            @RequestBody ReviewCommentDTO reviewCommentDTO,
            @PathVariable("customerId") UUID customerId,
            @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            if (UserDetailsUtils.nonAdmin(authenticatedUser)
                    && !Objects.equals(customerId,
                            productReviewService.findCustomerIdById(reviewId))) {
                logger.info("Unauthorized user: %s attempted to delete a resource",
                        ((UserDetailsId) authenticatedUser).getUserId().toString());
                return ResponseEntity.status(401).build();
            }
            ReviewComment reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
            reviewComment.setProductReviewId(reviewId);
            reviewCommentService.addReviewComment(reviewComment);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments/{commentId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerId)")
    public ResponseEntity<String> deleteReviewComment(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") UUID reviewId, @PathVariable("commentId") UUID commentId,
            @PathVariable("customerId") UUID customerId,
            @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            if (UserDetailsUtils.nonAdmin(authenticatedUser)
                    && !Objects.equals(customerId,
                            reviewCommentService.findCustomerIdById(commentId))) {
                logger.info("Unauthorized user: %s attempted to delete a resource",
                        ((UserDetailsId) authenticatedUser).getUserId().toString());
                return ResponseEntity.status(401).build();
            }
            reviewCommentService.deleteReviewComment(new ReviewComment(commentId));
            return ResponseEntity.noContent().build();
        } catch (ReviewCommentNotFoundException reviewCommentNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/products/{productId}/reviews/{reviewId}/comments/{commentId}", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> updateReviewComment(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") UUID reviewId, @PathVariable("commentId") UUID commentId,
            @RequestBody ReviewCommentDTO reviewCommentDTO) {
        try {
            ReviewComment reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
            reviewComment.setId(commentId);
            reviewComment.setProductReviewId(reviewId);
            reviewCommentService.updateReviewComment(reviewComment);
            return ResponseEntity.noContent().build();
        } catch (ReviewCommentNotFoundException reviewCommentNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/products/{productId}/reviews/{reviewId}/comments/{commentId}", produces = "application/json")
    public ResponseEntity<String> getReviewComment(@PathVariable("productId") UUID productId,
            @PathVariable("reviewId") BigInteger reviewId, @PathVariable("commentId") UUID commentId) {
        try {
            ReviewComment reviewComment = reviewCommentService
                    .getReviewComment(commentId);
            ReviewCommentDTO reviewCommentDTO = modelMapper.map(reviewComment, ReviewCommentDTO.class);
            return ResponseEntity.ok(new Gson().toJson(reviewCommentDTO));
        } catch (ReviewCommentNotFoundException reviewCommentNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
