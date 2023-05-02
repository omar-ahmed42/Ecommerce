package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.exception.CartItemNotFoundException;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.CartItemsRepository;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.CartService;

@SpringBootTest
public class CartServiceImplTest {

    @Autowired
    private CartService cartService;

    @SpyBean
    private CartItemsRepository cartItemsRepository;

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

    private User user;
    private Customer customer;

    private User admin;

    private Vendor vendor;
    private Product product;

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
        cartItemsRepository.deleteAll();
        productRepository.deleteAll();
        vendorRepository.delete(vendor);
        customerRepository.delete(customer);
        userRepository.delete(user);
        userRepository.delete(admin);
        roleRepository.deleteAll();
        user = null;
        customer = null;
        vendor = null;
        reset(cartItemsRepository);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addCartItem_ValidAsCustomer() {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setPrice(product.getPrice());
        cartItemDTO.setQuantity(3);

        cartService.addCartItem(cartItemDTO);

        Cartitems actual = cartItemsRepository.findById(new CartitemsPK(customer.getId(), product.getId())).get();

        Cartitems expected = new Cartitems(customer.getId(), product.getId());
        expected.setPrice(product.getPrice().setScale(2));
        expected.setQuantity(3);
        expected.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(3).setScale(2)));
        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt", "customerByCustomerId", "productByProductId")
                .isEqualTo(expected);

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addCartItem_EmptyQuantity_ThrowsMissingFieldException() {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setPrice(product.getPrice());
        cartItemDTO.setQuantity(null);

        org.junit.jupiter.api.Assertions.assertThrows(MissingFieldException.class,
                () -> cartService.addCartItem(cartItemDTO), "Quantity is missing");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void addCartItem_PriceLessThanZero_ThrowsInvalidInputException() {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setPrice(BigDecimal.valueOf(-1));
        cartItemDTO.setQuantity(3);

        org.junit.jupiter.api.Assertions.assertThrows(InvalidInputException.class,
                () -> cartService.addCartItem(cartItemDTO), "Price cannot be less than 0");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void deleteCartItem_NonExistentCartItem_ThrowsCartItemNotFoundException() {
        UUID randomId = UUID.randomUUID();
        org.junit.jupiter.api.Assertions.assertThrows(CartItemNotFoundException.class,
                () -> cartService.deleteCartItem(randomId), "Cart item not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void deleteCartItem_ValidAsCustomer() {
        Cartitems cartItem = new Cartitems(customer.getId(), product.getId());
        cartItem.setQuantity(3);
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(3)).setScale(2));
        cartItem.setPrice(product.getPrice());

        cartItem = cartItemsRepository.saveAndFlush(cartItem);

        cartService.deleteCartItem(product.getId());

        verify(cartItemsRepository).delete(any(Cartitems.class));

        long actual = cartItemsRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(0L, actual);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.admin@test.imagination")
    public void getCartItem_NonExistentCartItem_ThrowsCartItemNotFoundException() {
        UUID randomId = UUID.randomUUID();
        UUID randomId2 = UUID.randomUUID();
        org.junit.jupiter.api.Assertions.assertThrows(CartItemNotFoundException.class,
                () -> cartService.getCartItem(randomId, randomId2), "Cart item not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void getCartItem_ValidAsCustomer() {
        CartItemDTO expected = new CartItemDTO();
        expected.setProductId(product.getId());
        expected.setPrice(product.getPrice().setScale(2));
        expected.setQuantity(3);

        Cartitems cartItem = new Cartitems(customer.getId(), product.getId());
        cartItem.setQuantity(3);
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(3)).setScale(2));
        cartItem.setPrice(product.getPrice());

        cartItem = cartItemsRepository.saveAndFlush(cartItem);

        CartItemDTO actual = cartService.getCartItem(customer.getId(), product.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    public void updateCartItem_ZeroQuantity_ValidAsCustomer() {
        Cartitems cartItem = new Cartitems(customer.getId(), product.getId());
        cartItem.setQuantity(3);
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(3)).setScale(2));
        cartItem.setPrice(product.getPrice());

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setPrice(product.getPrice().setScale(2));
        cartItemDTO.setQuantity(0);

        cartItem = cartItemsRepository.saveAndFlush(cartItem);
        cartService.updateCartItem(product.getId(), cartItemDTO);

        verify(cartItemsRepository).deleteById(any(CartitemsPK.class));
        long actual = cartItemsRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(0L, actual);
    }
}
