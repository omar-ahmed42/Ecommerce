package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import com.omarahmed42.ecommerce.DTO.ProductReviewRequest;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductReview;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.ProductReviewRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.ProductReviewService;

@SpringBootTest
public class ProductReviewServiceImplTest {

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @SpyBean
    private ProductReviewRepository productReviewRepository;

    private User user;
    private Customer customer;

    private User admin;

    private Vendor vendor;
    private Product product;

    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;

    @BeforeEach
    public void init() {
        Role role = new Role("CUSTOMER");
        role = roleRepository.save(role);

        user = prepareUser();
        user.getRoles().add(role);

        user = userRepository.saveAndFlush(user);

        Role adminRole = new Role("ADMIN");
        adminRole = roleRepository.saveAndFlush(adminRole);

        admin = prepareUser("not.a.real.email.admin@test.imagination");
        admin.getRoles().add(adminRole);
        admin = userRepository.saveAndFlush(admin);

        vendor = new Vendor();
        vendor.setId(user.getId());
        vendor.setVerifiedVendor(true);
        vendor = vendorRepository.saveAndFlush(vendor);

        customer = new Customer(user.getId());
        customer = customerRepository.saveAndFlush(customer);

        product = prepareProduct();
        product.setVendorId(vendor.getId());
        product = productRepository.saveAndFlush(product);
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
        productReviewRepository.deleteAll();
        productRepository.deleteAll();
        vendorRepository.delete(vendor);
        customerRepository.delete(customer);
        userRepository.delete(user);
        user = null;
        customer = null;
        vendor = null;
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addProductReview_EmptyRating_ThrowsMissingFieldException() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        UUID productId = product.getId();
        Assertions.assertThrows(MissingFieldException.class,
                () -> productReviewService.addProductReview(productId, productReviewRequest),
                "Product review rating is missing");

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addProductReview_RatingLessThanZero_ThrowsInvalidInputException() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setRating(-1);

        UUID productId = product.getId();
        String expectedMessage = "rating must be between %d and %d".formatted(MIN_RATING, MAX_RATING);
        Assertions.assertThrows(InvalidInputException.class,
                () -> productReviewService.addProductReview(productId, productReviewRequest),
                expectedMessage);

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addProductReview_RatingBiggerThanFive_ThrowsInvalidInputException() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setRating(6);

        UUID productId = product.getId();
        String expectedMessage = "rating must be between %d and %d".formatted(MIN_RATING, MAX_RATING);
        Assertions.assertThrows(InvalidInputException.class,
                () -> productReviewService.addProductReview(productId, productReviewRequest),
                expectedMessage);

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addProductReview_ZeroRating_ShouldSucceed() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setRating(0);

        UUID productId = product.getId();
        productReviewService.addProductReview(productId, productReviewRequest);
        verify(productReviewRepository).save(any(ProductReview.class));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addProductReview_FiveRating_ShouldSucceed() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setRating(5);

        UUID productId = product.getId();
        productReviewService.addProductReview(productId, productReviewRequest);
        verify(productReviewRepository).save(any(ProductReview.class));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void deleteProductReview_AsProductReviewOwner() {
        ProductReview productReview = new ProductReview();
        productReview.setProductId(product.getId());
        productReview.setCustomerId(customer.getId());
        productReview.setRating(4);

        productReview = productReviewRepository.saveAndFlush(productReview);

        productReviewService.deleteProductReview(productReview.getId());
        verify(productReviewRepository).delete(any(ProductReview.class));

        long actual = productReviewRepository.count();
        Assertions.assertEquals(0L, actual);
    }

}
