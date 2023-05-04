package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import com.omarahmed42.ecommerce.DTO.ReviewCommentRequest;
import com.omarahmed42.ecommerce.DTO.ReviewCommentResponse;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.ProductReviewNotFoundException;
import com.omarahmed42.ecommerce.exception.ReviewCommentNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.model.ReviewComment;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.ProductReviewRepository;
import com.omarahmed42.ecommerce.repository.ReviewCommentRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.ReviewCommentService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ReviewCommentServiceImplTest {
    @Autowired
    private ReviewCommentService reviewCommentService;

    @SpyBean
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @SpyBean
    private ProductReviewRepository productReviewRepository;

    private User user;
    private User admin;
    private Vendor vendor;
    private Customer customer;
    private Product product;
    private ProductReview productReview;

    private User differentUser;
    private Customer differentCustomer;

    @BeforeAll
    public void init() {
        Role customerRole = new Role("CUSTOMER");
        customerRole = roleRepository.save(customerRole);

        user = prepareUser();
        user.getRoles().add(customerRole);

        user = userRepository.saveAndFlush(user);

        Role adminRole = new Role("ADMIN");
        adminRole = roleRepository.saveAndFlush(adminRole);

        admin = prepareUser("not.a.real.email.admin@test.imagination");
        admin.getRoles().add(adminRole);
        admin = userRepository.saveAndFlush(admin);

        differentUser = prepareUser("not.a.real.email.different.customer@test.imagination");
        differentUser.getRoles().add(customerRole);
        differentUser = userRepository.saveAndFlush(differentUser);

        vendor = new Vendor();
        vendor.setId(user.getId());
        vendor.setVerifiedVendor(true);
        vendor = vendorRepository.saveAndFlush(vendor);

        customer = new Customer(user.getId());
        customer = customerRepository.saveAndFlush(customer);

        differentCustomer = new Customer(differentUser.getId());
        differentCustomer = customerRepository.saveAndFlush(differentCustomer);

        product = prepareProduct();
        product.setVendor(vendor);
        product = productRepository.saveAndFlush(product);

        productReview = new ProductReview();
        productReview.setProduct(product);
        productReview.setCustomer(customer);
        productReview.setRating(4);

        productReview = productReviewRepository.saveAndFlush(productReview);
    }

    private User prepareUser() {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("not.a.real.email.customer@test.imagination");
        user.setPassword("user_pass");
        user.setPhoneNumber("0123456789");
        user.setActive(false);
        user.setBanned(false);
        return user;
    }

    private User prepareUser(String email) {
        User user = prepareUser();
        user.setEmail(email);
        return user;
    }

    private Product prepareProduct() {
        Product product = new Product();
        product.setName("Modern phone");
        product.setDescription("This is a description");
        product.setPrice(BigDecimal.ONE);
        product.setStock(5);
        product.setRating(0D);
        return product;
    }

    @AfterEach
    public void tearDown() {
        reviewCommentRepository.deleteAll();
        reset(productReviewRepository, reviewCommentRepository);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addReviewComment_EmptyTitle_ThrowsMissingFieldException() {
        UUID reviewId = productReview.getId();
        ReviewCommentRequest reviewCommentRequest = new ReviewCommentRequest();
        reviewCommentRequest.setContent("This is my content");
        Assertions.assertThrows(MissingFieldException.class,
                () -> reviewCommentService.addReviewComment(reviewId, reviewCommentRequest), "Review title is missing");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addReviewComment_EmptyContent_ThrowsMissingFieldException() {
        UUID reviewId = productReview.getId();
        ReviewCommentRequest reviewCommentRequest = new ReviewCommentRequest();
        reviewCommentRequest.setTitle("My title");
        Assertions.assertThrows(MissingFieldException.class,
                () -> reviewCommentService.addReviewComment(reviewId, reviewCommentRequest),
                "Review content is missing");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addReviewComment_NonExistentProductReview_ThrowsProductReviewNotFoundException() {
        UUID randomReviewId = UUID.randomUUID();
        ReviewCommentRequest reviewCommentRequest = new ReviewCommentRequest();
        reviewCommentRequest.setTitle("My title");
        reviewCommentRequest.setContent("This is my content");
        Assertions.assertThrows(ProductReviewNotFoundException.class,
                () -> reviewCommentService.addReviewComment(randomReviewId, reviewCommentRequest),
                "Product review not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.different.customer@test.imagination")
    void addReviewComment_NonProductReviewOwner_Unauthorized_ThrowsUnauthorizedAccessException() {
        UUID reviewId = productReview.getId();
        UUID unauthorizedUserId = differentCustomer.getId();

        ReviewCommentRequest reviewCommentRequest = new ReviewCommentRequest();
        reviewCommentRequest.setTitle("My title");
        reviewCommentRequest.setContent("This is my content");

        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> reviewCommentService.addReviewComment(reviewId, reviewCommentRequest),
                "Unauthorized user with id " + unauthorizedUserId + " tried to add a review comment");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addReviewComment_Valid_ReturnsUUID() {
        UUID reviewId = productReview.getId();
        ReviewCommentRequest reviewCommentRequest = new ReviewCommentRequest();
        reviewCommentRequest.setTitle("My title");
        reviewCommentRequest.setContent("This is my content");

        long countBefore = reviewCommentRepository.count();
        Assertions.assertEquals(0L, countBefore);

        UUID actual = reviewCommentService.addReviewComment(reviewId, reviewCommentRequest);
        UUID expected = reviewCommentRepository.findById(actual).get().getId();

        verify(productReviewRepository).findById(any(UUID.class));
        verify(reviewCommentRepository).save(any(ReviewComment.class));

        Assertions.assertEquals(1L, reviewCommentRepository.count());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void deleteReviewComment_NonExistentReviewComment_ThrowsReviewCommentNotFoundException() {
        UUID reviewCommentId = UUID.randomUUID();
        Assertions.assertThrows(ReviewCommentNotFoundException.class,
                () -> reviewCommentService.deleteReviewComment(reviewCommentId), "Review comment not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.different.customer@test.imagination")
    void deleteReviewComment_NonOwner_Unauthorized_ThrowsUnauthorizedAccessException() {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setTitle("My title");
        reviewComment.setContent("This is my content");
        reviewComment.setProductReview(productReview);
        reviewComment = reviewCommentRepository.saveAndFlush(reviewComment);

        UUID reviewId = reviewComment.getId();
        UUID unauthorizedUserId = differentCustomer.getId();
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> reviewCommentService.deleteReviewComment(reviewId),
                "Unauthorized user with id " + unauthorizedUserId + " tried to delete a review comment");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void deleteReviewComment_Valid_DeletesReviewComment_CountShouldBeZero() {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setTitle("My title");
        reviewComment.setContent("This is my content");
        reviewComment.setProductReview(productReview);
        reviewComment = reviewCommentRepository.saveAndFlush(reviewComment);

        Assertions.assertEquals(1L, reviewCommentRepository.count());

        UUID reviewId = reviewComment.getId();
        reviewCommentService.deleteReviewComment(reviewId);

        verify(reviewCommentRepository).findById(any(UUID.class));
        verify(reviewCommentRepository).delete(any(ReviewComment.class));

        Assertions.assertEquals(0L, reviewCommentRepository.count());
        Assertions.assertEquals(false, reviewCommentRepository.existsById(reviewId));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void getReviewComment_ThrowsReviewCommentNotFoundException() {
        UUID randomId = UUID.randomUUID();
        Assertions.assertThrows(ReviewCommentNotFoundException.class,
                () -> reviewCommentService.getReviewComment(randomId), "Review comment not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void getReviewComment_ReturnsReviewCommentResponse() {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setTitle("My title");
        reviewComment.setContent("This is my content");
        reviewComment.setProductReview(productReview);
        reviewComment = reviewCommentRepository.saveAndFlush(reviewComment);

        ReviewCommentResponse actual = reviewCommentService.getReviewComment(reviewComment.getId());
        verify(reviewCommentRepository).findById(any(UUID.class));

        ReviewCommentResponse expected = new ReviewCommentResponse();

        expected.setTitle("My title");
        expected.setContent("This is my content");
        expected.setId(reviewComment.getId());

        Assertions.assertNotNull(actual.getCreatedAt());
        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(expected);
    }

}