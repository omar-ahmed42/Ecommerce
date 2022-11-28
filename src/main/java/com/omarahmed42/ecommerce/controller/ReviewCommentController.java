package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.omarahmed42.ecommerce.util.BigIntegerHandler;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@RestController
@RequestMapping("/v1")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;
    private final ProductReviewService productReviewService;
    private static final Logger logger = LogManager.getLogger(ReviewCommentController.class);
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ReviewCommentController(ReviewCommentService reviewCommentService,
            ProductReviewService productReviewService) {
        this.reviewCommentService = reviewCommentService;
        this.productReviewService = productReviewService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerId)")
    public ResponseEntity<String> addNewReviewComment(@PathVariable("productId") BigInteger productId,
            @PathVariable("reviewId") BigInteger reviewId,
            @RequestBody ReviewCommentDTO reviewCommentDTO,
            @PathVariable("customerId") BigInteger customerId,
            @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            if (UserDetailsUtils.nonAdmin(authenticatedUser)
                    && !Arrays.equals(BigIntegerHandler.toByteArray(customerId),
                            productReviewService.findCustomerIdById(BigIntegerHandler.toByteArray(reviewId)))) {
                logger.info("Unauthorized user: %d attempted to delete a resource",
                        ((UserDetailsId) authenticatedUser).getUserId());
                return ResponseEntity.status(401).build();
            }
            ReviewComment reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
            reviewComment.setProductReviewId(BigIntegerHandler.toByteArray(reviewId));
            reviewCommentService.addReviewComment(reviewComment);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments/{commentId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerId)")
    public ResponseEntity<String> deleteReviewComment(@PathVariable("productId") BigInteger productId,
            @PathVariable("reviewId") BigInteger reviewId, @PathVariable("commentId") BigInteger commentId,
            @PathVariable("customerId") BigInteger customerId,
            @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            if (UserDetailsUtils.nonAdmin(authenticatedUser)
                    && !Arrays.equals(BigIntegerHandler.toByteArray(customerId),
                            reviewCommentService.findCustomerIdById(BigIntegerHandler.toByteArray(commentId)))) {
                logger.info("Unauthorized user: %d attempted to delete a resource",
                        ((UserDetailsId) authenticatedUser).getUserId());
                return ResponseEntity.status(401).build();
            }
            reviewCommentService.deleteReviewComment(new ReviewComment(BigIntegerHandler.toByteArray(commentId)));
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
    public ResponseEntity<String> updateReviewComment(@PathVariable("productId") BigInteger productId,
            @PathVariable("reviewId") BigInteger reviewId, @PathVariable("commentId") BigInteger commentId,
            @RequestBody ReviewCommentDTO reviewCommentDTO) {
        try {
            ReviewComment reviewComment = modelMapper.map(reviewCommentDTO, ReviewComment.class);
            reviewComment.setId(BigIntegerHandler.toByteArray(commentId));
            reviewComment.setProductReviewId(BigIntegerHandler.toByteArray(reviewId));
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
    public ResponseEntity<String> getReviewComment(@PathVariable("productId") BigInteger productId,
            @PathVariable("reviewId") BigInteger reviewId, @PathVariable("commentId") BigInteger commentId) {
        try {
            ReviewComment reviewComment = reviewCommentService
                    .getReviewComment(BigIntegerHandler.toByteArray(commentId));
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
