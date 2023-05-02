package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.exception.WishlistAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.WishlistNotFoundException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.repository.WishlistRepository;
import com.omarahmed42.ecommerce.service.WishlistService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class WishlistServiceImplTest {

    @Autowired
    private WishlistService wishlistService;

    @SpyBean
    private WishlistRepository wishlistRepository;

    private Customer customer;
    private Product product;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void init() {
        User customerUser = prepareUser("not.a.real.email.customer@test.imagination", "CUSTOMER");
        User vendorUser = prepareUser("not.a.real.email.vendor@test.imagination", "VERIFIED_VENDOR");
        customer = new Customer(customerUser.getId());
        customer = customerRepository.saveAndFlush(customer);
        Vendor vendor = new Vendor(vendorUser.getId());
        vendor.setVerifiedVendor(true);
        vendor = vendorRepository.saveAndFlush(vendor);

        product = new Product();
        product.setDescription("This is a description");
        product.setName("Modern phone");
        product.setRating(4D);
        product.setStock(5);
        product.setPrice(BigDecimal.ONE.setScale(2));
        product.setVendorId(vendor.getId());
        product = productRepository.saveAndFlush(product);
    }

    private User prepareUser(String email, String roleName) {
        User user = new User();
        user.setActive(true);
        user.setEnabled(true);
        user.setVerified(true);
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail(email);
        user.setPassword("user_pass");
        user.setPhoneNumber("0123456789");
        Role role = new Role(roleName);
        role = roleRepository.saveAndFlush(role);
        user.getRoles().add(role);
        user = userRepository.saveAndFlush(user);
        return user;
    }

    @AfterEach
    public void tearDown() {
        wishlistRepository.deleteAll();
        reset(wishlistRepository);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addWishlist_DuplicateWishlists_ThrowsWishlistAlreadyExistsException() {
        doReturn(true).when(wishlistRepository).existsById(any(WishlistPK.class));
        UUID productId = product.getId();
        Assertions.assertThrows(WishlistAlreadyExistsException.class, () -> wishlistService.addWishlist(productId),
                "Wishlist item already exists");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addWishlist_Valid_SavesWishlist() {

        Assertions.assertEquals(0L, wishlistRepository.count());

        wishlistService.addWishlist(product.getId());

        verify(wishlistRepository).existsById(any(WishlistPK.class));
        verify(wishlistRepository).save(any(Wishlist.class));

        long actual = wishlistRepository.count();
        Assertions.assertEquals(1, actual);
    }

    @Test
    @WithAnonymousUser
    public void addWishlist_AnonymousUser_ThrowsAccessDeniedException() {
        UUID productId = product.getId();
        Assertions.assertThrows(AccessDeniedException.class, () -> wishlistService.addWishlist(productId));
    }

    @Test
    @WithMockUser(roles = "VERIFIED_VENDOR")
    public void addWishlist_AuthenticatedButNotAuthorized_VerifiedVendor_ThrowsAccessDeniedException() {
        UUID productId = product.getId();
        Assertions.assertThrows(AccessDeniedException.class, () -> wishlistService.addWishlist(productId));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void deleteWishlist_ThrowsWishlistNotFoundException() {
        UUID productId = product.getId();
        Assertions.assertThrows(WishlistNotFoundException.class, () -> wishlistService.deleteWishlist(productId),
                "Wishlist not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void deleteWishlist_ExistingWishlist_Valid() {
        UUID productId = product.getId();
        wishlistRepository.save(new Wishlist(customer.getId(), productId));
        Assertions.assertEquals(1L, wishlistRepository.count());

        wishlistService.deleteWishlist(productId);

        verify(wishlistRepository).findById(any(WishlistPK.class));
        verify(wishlistRepository).delete(any(Wishlist.class));
        long actual = wishlistRepository.count();
        Assertions.assertEquals(0L, actual);
    }

    @Test
    @WithMockUser(roles = "VERIFIED_VENDOR")
    public void deleteWishlist_AuthenticatedButNotAuthorized_VerifiedVendor_ThrowsAccessDeniedException() {
        UUID productId = product.getId();
        Assertions.assertThrows(AccessDeniedException.class, () -> wishlistService.deleteWishlist(productId));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void getWishlist_ExistingWishlist_Valid_ReturnsProductResponse() {
        UUID productId = product.getId();
        wishlistRepository.saveAndFlush(new Wishlist(customer.getId(), productId));
        Assertions.assertEquals(1L, wishlistRepository.count());

        ProductResponse actual = wishlistService.getWishlist(customer.getId(), productId);
        verify(wishlistRepository).findProductByWishlistPK(any(WishlistPK.class));

        ProductResponse expected = new ProductResponse();
        BeanUtils.copyProperties(product, expected, "createdAt");
        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(expected);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void getWishlist_NotFound_ThrowsWishlistNotFoundException() {
        UUID customerId = customer.getId();
        UUID productId = product.getId();
        Assertions.assertThrows(WishlistNotFoundException.class,
                () -> wishlistService.getWishlist(customerId, productId), "Wishlist not found");
    }

}
