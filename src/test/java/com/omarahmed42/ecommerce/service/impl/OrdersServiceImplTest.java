package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import com.omarahmed42.ecommerce.DTO.BillingAddressDTO;
import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.OrderNotFoundException;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.BillingAddressRepository;
import com.omarahmed42.ecommerce.repository.CustomerOrdersRepository;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.OrderRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.OrdersService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class OrdersServiceImplTest {

    @Autowired
    private OrdersService orderService;

    @SpyBean
    private OrderRepository orderRepository;

    private Customer customer;
    private Product product;
    private BillingAddress billingAddress;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private ProductRepository productRepository;

    @SpyBean
    private CustomerRepository customerRepository;

    @SpyBean
    private VendorRepository vendorRepository;

    @SpyBean
    private RoleRepository roleRepository;

    @SpyBean
    private BillingAddressRepository billingAddressRepository;

    @SpyBean
    private CustomerOrdersRepository customerOrderRepository;

    @BeforeAll
    void init() {
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

        billingAddress = new BillingAddress();
        billingAddress.setPostalCode("121");
        billingAddress.setCountry("FakeCountry");
        billingAddress.setAddress("Fake address");
        billingAddress = billingAddressRepository.saveAndFlush(billingAddress);

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
    void tearDown() {
        customerOrderRepository.deleteAll();
        orderRepository.deleteAll();
        reset(orderRepository, billingAddressRepository, productRepository, userRepository, roleRepository);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addOrder_NoProducts_ThrowsMissingFieldException() {
        UUID customerId = customer.getId();
        Assertions.assertThrows(MissingFieldException.class, () -> orderService.addOrder(customerId, null, null),
                "Products ids are missing");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void addOrder_Valid() {
        UUID customerId = customer.getId();
        CartItemDTO cartItem = new CartItemDTO();
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(3);
        cartItem.setPrice(product.getPrice());

        CartItemDTO[] items = new CartItemDTO[1];
        items[0] = cartItem;

        BillingAddressDTO billingAddressDTO = new BillingAddressDTO();
        billingAddressDTO.setPostalCode("121");
        billingAddressDTO.setCountry("FakeCountry");
        billingAddressDTO.setAddress("Fake address");

        Orders actual = orderService.addOrder(customerId, items, billingAddressDTO);
        Orders expected = new Orders();
        expected.setId(actual.getId());
        expected.setTotalPrice(BigDecimal.valueOf(3).setScale(2));
        expected.setStatus(Status.PENDING);

        verify(productRepository).findAllById(ArgumentMatchers.<UUID>anySet());
        verify(productRepository).saveAll(anyIterable());
        verify(billingAddressRepository).save(any(BillingAddress.class));

        verify(orderRepository).save(any(Orders.class));
        verify(customerOrderRepository).save(any(CustomerOrders.class));
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("purchaseDate", "billingAddressId", "createdAt", "customerOrdersById", "payment",
                        "productItemsById",
                        "billingAddressByBillingAddressId")
                .isEqualTo(expected);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void getOrders_ThrowsOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.empty()).when(orderRepository).findById(any(UUID.class));
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(orderId), "Order not found");
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "customUserDetailsService", value = "not.a.real.email.customer@test.imagination")
    void getOrders_Valid_ShouldReturnOrder() {
        Orders expected = saveOrder();
        UUID orderId = expected.getId();

        Orders actual = orderService.getOrder(orderId);
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "customerOrdersById", "payment", "productItemsById",
                        "billingAddressByBillingAddressId", "purchaseDate")
                .isEqualTo(expected);
    }

    private Orders saveOrder() {
        Orders orders = new Orders();
        orders.setBillingAddressId(billingAddress.getId());
        orders.setPurchaseDate(Instant.now());
        orders.setTotalPrice(BigDecimal.ONE.setScale(2));
        orders.setStatus(Status.PENDING);
        orders = orderRepository.saveAndFlush(orders);
        reset(orderRepository);
        return orders;
    }

    @Test
    void updateOrderPartially_NonExistentOrder_ThrowsOrderNotFoundException() {
        UUID orderId = UUID.randomUUID();

        OrderDetailsDTO emptyOrderDetails = OrderDetailsDTO.builder().build();
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderPartially(orderId, emptyOrderDetails),
                "Order not found");
    }

    @Test
    void updateOrderPartially_PriceLessThanZero_ThrowsInvalidInputException() {
        OrderDetailsDTO orderDetails = OrderDetailsDTO.builder().totalPrice(BigDecimal.valueOf(-1))
                .purchaseDate(Instant.now()).billingAddressId(
                        billingAddress.getId())
                .status(Status.PENDING).build();
        UUID orderId = UUID.randomUUID();
        Assertions.assertThrows(InvalidInputException.class,
                () -> orderService.updateOrderPartially(orderId, orderDetails), "Total price cannot be less than 0");
    }

    @Test
    void updateOrderPartially_WithoutTotalPrice_ShouldUpdateSuccessfully() {
        OrderDetailsDTO orderDetails = OrderDetailsDTO.builder()
                .purchaseDate(Instant.now()).billingAddressId(
                        billingAddress.getId())
                .status(Status.CANCELLED).build();
        Orders order = saveOrder();
        UUID orderId = order.getId();

        Orders expected = new Orders();
        BeanUtils.copyProperties(order, expected);
        expected.setStatus(Status.CANCELLED);

        orderService.updateOrderPartially(orderId, orderDetails);
        verify(orderRepository).save(any(Orders.class));

        Orders actual = orderRepository.findById(orderId).get();

        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("createdAt", "customerOrdersById", "payment", "productItemsById",
                        "billingAddressByBillingAddressId", "purchaseDate")
                .isEqualTo(expected);

    }

    @Test
    void deleteOrder_NonExistentOrder_ThrowsOrderNotFound() {
        UUID randomOrderId = UUID.randomUUID();
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(randomOrderId),
                "Order not found");
    }

    @Test
    void deleteOrder_ValidOrderId_ShouldBeDeleted() {
        Orders orders = saveOrder();

        long countBefore = orderRepository.count();
        Assertions.assertEquals(1L, countBefore);

        orderService.deleteOrder(orders.getId());
        verify(orderRepository).delete(any(Orders.class));

        long actual = orderRepository.count();
        Assertions.assertEquals(0L, actual);
    }
}
