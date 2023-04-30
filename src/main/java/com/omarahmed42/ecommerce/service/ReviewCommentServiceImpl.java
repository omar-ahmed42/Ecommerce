package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.ReviewCommentRequest;
import com.omarahmed42.ecommerce.DTO.ReviewCommentResponse;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.ProductReviewNotFoundException;
import com.omarahmed42.ecommerce.exception.ReviewCommentNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.model.ReviewComment;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.ProductReviewRepository;
import com.omarahmed42.ecommerce.repository.ReviewCommentRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ProductReviewRepository productReviewRepository;
    private ModelMapper modelMapper;

    public ReviewCommentServiceImpl(ReviewCommentRepository reviewCommentRepository,
            ProductReviewRepository productReviewRepository) {
        this.reviewCommentRepository = reviewCommentRepository;
        this.productReviewRepository = productReviewRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.CUSTOMER.toString())")
    public UUID addReviewComment(UUID reviewId, ReviewCommentRequest reviewCommentRequest) {
        validateReviewComment(reviewCommentRequest);
        ProductReview productReview = productReviewRepository.findById(reviewId)
                .orElseThrow(ProductReviewNotFoundException::new);
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!isOwner(productReview, authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "add", "a review comment");
        ReviewComment reviewComment = modelMapper.map(reviewCommentRequest, ReviewComment.class);
        reviewComment.setProductReviewId(reviewId);
        reviewComment.setProductReviewByProductReviewId(productReview);
        reviewComment = reviewCommentRepository.save(reviewComment);
        return reviewComment.getId();
    }

    private boolean isOwner(ProductReview productReview, UUID userId) {
        return productReview.getCustomerId().equals(userId);
    }

    private void validateReviewComment(ReviewCommentRequest reviewCommentRequest) {
        if (StringUtils.isBlank(reviewCommentRequest.getTitle()))
            throw new MissingFieldException("Review title is missing");
        if (StringUtils.isBlank(reviewCommentRequest.getContent()))
            throw new MissingFieldException("Review content is missing");
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.ADMIN.toString()) || hasRole(Role.CUSTOMER.toString())")
    public void deleteReviewComment(UUID id) {
        ReviewComment reviewComment = reviewCommentRepository
                .findById(id).orElseThrow(ReviewCommentNotFoundException::new);
        UUID commentOwnerId = reviewComment.getProductReviewByProductReviewId().getCustomerId();
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!UserDetailsUtils.hasRole(Role.ADMIN) && !commentOwnerId.equals(authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "delete", "a review comment");
        reviewCommentRepository.delete(reviewComment);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.CUSTOMER.toString())")
    public void updateReviewComment(UUID id, ReviewCommentRequest reviewCommentRequest) {
        validateReviewComment(reviewCommentRequest);
        ReviewComment reviewComment = reviewCommentRepository
                .findById(id)
                .orElseThrow(ReviewCommentNotFoundException::new);
        UUID commentOwnerId = reviewComment.getProductReviewByProductReviewId().getCustomerId();
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!commentOwnerId.equals(authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "update", "a review comment");
        reviewComment = modelMapper.map(reviewCommentRequest, ReviewComment.class);
        reviewCommentRepository.save(reviewComment);
    }

    @Override
    @Transactional
    public ReviewCommentResponse getReviewComment(UUID commentId) {
        ReviewComment reviewComment = reviewCommentRepository
                .findById(commentId)
                .orElseThrow(ReviewCommentNotFoundException::new);
        return modelMapper.map(reviewComment, ReviewCommentResponse.class);
    }
}