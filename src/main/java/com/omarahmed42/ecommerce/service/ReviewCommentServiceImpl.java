package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.access.prepost.PreAuthorize;
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
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public UUID addReviewComment(UUID reviewId, ReviewCommentRequest reviewCommentRequest) {
        validateReviewComment(reviewCommentRequest);
        System.out.println("UUID: " + reviewId);
        System.out.println("UUID_to_string: " + reviewId.toString());
        System.out.println("UUID Class: " + reviewId.getClass());
        ProductReview productReview = productReviewRepository.findById(reviewId)
                .orElseThrow(ProductReviewNotFoundException::new);
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!isOwner(productReview, authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "add", "a review comment");
        ReviewComment reviewComment = modelMapper.map(reviewCommentRequest, ReviewComment.class);
        reviewComment.setProductReview(productReview);
        reviewComment = reviewCommentRepository.save(reviewComment);
        return reviewComment.getId();
    }

    private boolean isOwner(ProductReview productReview, UUID userId) {
        return productReview.getCustomer().getId().equals(userId);
    }

    private void validateReviewComment(ReviewCommentRequest reviewCommentRequest) {
        if (StringUtils.isBlank(reviewCommentRequest.getTitle()))
            throw new MissingFieldException("Review title is missing");
        if (StringUtils.isBlank(reviewCommentRequest.getContent()))
            throw new MissingFieldException("Review content is missing");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('CUSTOMER')")
    public void deleteReviewComment(UUID id) {
        ReviewComment reviewComment = reviewCommentRepository
                .findById(id).orElseThrow(ReviewCommentNotFoundException::new);
        UUID commentOwnerId = reviewComment.getProductReview().getCustomer().getId();
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!UserDetailsUtils.hasRole(Role.ADMIN) && !commentOwnerId.equals(authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "delete", "a review comment");
        reviewCommentRepository.delete(reviewComment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void updateReviewComment(UUID id, ReviewCommentRequest reviewCommentRequest) {
        validateReviewComment(reviewCommentRequest);
        ReviewComment reviewComment = reviewCommentRepository
                .findById(id)
                .orElseThrow(ReviewCommentNotFoundException::new);
        UUID commentOwnerId = reviewComment.getProductReview().getCustomer().getId();
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!commentOwnerId.equals(authenticatedUser.getId()))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "update", "a review comment");
        modelMapper.map(reviewCommentRequest, reviewComment);
        reviewCommentRepository.save(reviewComment);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewCommentResponse getReviewComment(UUID commentId) {
        ReviewComment reviewComment = reviewCommentRepository
                .findById(commentId)
                .orElseThrow(ReviewCommentNotFoundException::new);
        return modelMapper.map(reviewComment, ReviewCommentResponse.class);
    }
}