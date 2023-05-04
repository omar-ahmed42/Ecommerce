package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.util.Pair;

import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.enums.PaymentStatus;
import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.OrderDetails;
import com.omarahmed42.ecommerce.model.Payment;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.BillingAddressRepository;
import com.omarahmed42.ecommerce.repository.CustomerOrdersRepository;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.OrderDetailsRepository;
import com.omarahmed42.ecommerce.repository.PaymentRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.RoleRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.OrderDetailsService;
import com.omarahmed42.ecommerce.service.PaymentService;
import com.stripe.model.PaymentIntent;

@SpringBootTest
public class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @SpyBean
    private PaymentRepository paymentRepository;

    @SpyBean
    private OrderDetailsService orderService;

    @SpyBean
    private OrderDetailsRepository orderRepository;

    private Customer customer;
    private Product product;
    private BillingAddress billingAddress;
    private OrderDetails order;

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

    @BeforeEach
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
        product.setVendor(vendor);
        product = productRepository.saveAndFlush(product);

        billingAddress = new BillingAddress();
        billingAddress.setPostalCode("121");
        billingAddress.setCountry("FakeCountry");
        billingAddress.setAddress("Fake address");
        billingAddress = billingAddressRepository.saveAndFlush(billingAddress);

        order = saveOrder();
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

    private OrderDetails saveOrder() {
        OrderDetails orders = new OrderDetails();
        orders.setBillingAddress(billingAddress);
        orders.setPurchaseDate(Instant.now());
        orders.setTotalPrice(BigDecimal.ONE.setScale(2));
        orders.setStatus(Status.PENDING);
        orders = orderRepository.saveAndFlush(orders);
        reset(orderRepository);
        return orders;
    }

    @AfterEach
    void tearDown() {
        customerOrderRepository.deleteAll();
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        billingAddressRepository.deleteAll();
        customerRepository.deleteAll();
        vendorRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        reset(orderRepository, billingAddressRepository, productRepository, userRepository, roleRepository,
                paymentRepository, orderService);
    }

    @Test
    void handlePaymentIntent_handlePaymentCreated_AlreadyExistsShouldReturn() {
        doReturn(true).when(paymentRepository).existsById(any(String.class));

        PaymentIntent paymentIntent = preparePaymentIntent();

        paymentService.handlePaymentIntent("payment_intent.created", paymentIntent);
        verify(paymentRepository).existsById(any(String.class));
        verifyNoMoreInteractions(paymentRepository);
    }

    private Map<String, String> prepareMetadata(Pair<String, String>... pairs) {
        Map<String, String> metadata = new HashMap<>();
        for (Pair<String, String> pair : pairs) {
            metadata.put(pair.getFirst(), pair.getSecond());
        }

        return metadata;
    }

    PaymentIntent preparePaymentIntent() {
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setMetadata(prepareMetadata(Pair.of("orderId", order.getId().toString())));
        paymentIntent.setId(UUID.randomUUID().toString());
        paymentIntent.setAmount(1L);
        return paymentIntent;
    }

    @Test
    void handlePayment_Intent_handlePaymentCreated_SavesPayment() {
        PaymentIntent paymentIntent = preparePaymentIntent();

        Assertions.assertEquals(0L, paymentRepository.count());

        paymentService.handlePaymentIntent("payment_intent.created", paymentIntent);
        verify(paymentRepository).save(any(Payment.class));

        Assertions.assertEquals(1L, paymentRepository.count());

        Payment actual = paymentRepository.findById(paymentIntent.getId()).get();

        Payment expected = new Payment();
        expected.setOrderDetails(order);
        expected.setPaymentIntentId(paymentIntent.getId());
        expected.setPaymentStatus(PaymentStatus.CREATED);
        expected.setPaymentAmount(1L);

        Assertions.assertNotNull(actual.getCreatedAt());

        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderDetails", "createdAt").isEqualTo(expected);
    }

    @Test
    void handlePaymentIntentSucceeded_EmptyRetrievedPayment_ShouldReturn() {
        doReturn(Optional.empty()).when(paymentRepository).findById(any(String.class));
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    void handlePaymentIntentSucceeded_ShouldUpdatePaymentWithCOMPLETEDStatus() {
        PaymentIntent paymentIntent = preparePaymentIntent();
        String paymentId = paymentIntent.getId();

        Payment expectedPayment = new Payment();
        expectedPayment.setOrderDetails(order);
        expectedPayment.setPaymentStatus(PaymentStatus.PENDING);
        expectedPayment.setPaymentAmount(1L);
        expectedPayment.setPaymentIntentId(paymentId);
        expectedPayment = paymentRepository.saveAndFlush(expectedPayment);
        reset(paymentRepository);

        paymentService.handlePaymentIntent("payment_intent.succeeded", paymentIntent);
        verify(paymentRepository).findById(any(String.class));
        verify(orderService).updateOrderDetailsPartially(any(UUID.class), any(OrderDetailsDTO.class));
        verify(paymentRepository).save(any(Payment.class));

        expectedPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        Payment actual = paymentRepository.findById(paymentId).get();
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "orderDetails")
                .isEqualTo(expectedPayment);
    }

    @Test
    void handlePaymentIntentSucceeded_ShouldUpdateOrderWithCOMPLETEDStatus() {
        PaymentIntent paymentIntent = preparePaymentIntent();
        String paymentId = paymentIntent.getId();

        Payment expectedPayment = new Payment();
        expectedPayment.setOrderDetails(order);
        expectedPayment.setPaymentStatus(PaymentStatus.PENDING);
        expectedPayment.setPaymentAmount(1L);
        expectedPayment.setPaymentIntentId(paymentId);
        expectedPayment = paymentRepository.saveAndFlush(expectedPayment);
        reset(paymentRepository);

        paymentService.handlePaymentIntent("payment_intent.succeeded", paymentIntent);
        verify(paymentRepository).findById(any(String.class));
        verify(orderService).updateOrderDetailsPartially(any(UUID.class), any(OrderDetailsDTO.class));
        verify(paymentRepository).save(any(Payment.class));

        expectedPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        OrderDetails actual = orderRepository.findById(order.getId()).get();
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "customerOrdersById", "payment", "orderItems",
                        "billingAddress", "purchaseDate", "status")
                .isEqualTo(order);

        Assertions.assertEquals(Status.COMPLETED, actual.getStatus());
    }

    @Test
    void handlePaymentIntentFailed_NoPaymentStored_ShouldReturn() {
        doReturn(Optional.empty()).when(paymentRepository).findById(any(String.class));
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    void handlePaymentIntentFailed_ShouldUpdatePaymentStatusWithFAILED() {
        PaymentIntent paymentIntent = preparePaymentIntent();
        String paymentId = paymentIntent.getId();

        Payment expectedPayment = new Payment();
        expectedPayment.setOrderDetails(order);
        expectedPayment.setPaymentStatus(PaymentStatus.PENDING);
        expectedPayment.setPaymentAmount(1L);
        expectedPayment.setPaymentIntentId(paymentId);
        expectedPayment = paymentRepository.saveAndFlush(expectedPayment);
        reset(paymentRepository);

        paymentService.handlePaymentIntent("payment_intent.payment_failed", paymentIntent);

        verify(paymentRepository).findById(any(String.class));
        verify(orderService).updateOrderDetailsPartially(any(UUID.class), any(OrderDetailsDTO.class));
        verify(paymentRepository).save(any(Payment.class));
        verify(orderRepository).getReferenceById(any(UUID.class));
        verify(productRepository).saveAllAndFlush(anyList());

        expectedPayment.setPaymentStatus(PaymentStatus.FAILED);
        Payment actual = paymentRepository.findById(paymentId).get();
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "orderDetails")
                .isEqualTo(expectedPayment);
    }

    @Test
    void handlePaymentIntentFailed_ShouldUpdateOrderWithFAILEDStatus() {
        PaymentIntent paymentIntent = preparePaymentIntent();
        String paymentId = paymentIntent.getId();

        Payment expectedPayment = new Payment();
        expectedPayment.setOrderDetails(order);
        expectedPayment.setPaymentStatus(PaymentStatus.PENDING);
        expectedPayment.setPaymentAmount(1L);
        expectedPayment.setPaymentIntentId(paymentId);
        expectedPayment = paymentRepository.saveAndFlush(expectedPayment);
        reset(paymentRepository);

        paymentService.handlePaymentIntent("payment_intent.payment_failed", paymentIntent);
        verify(paymentRepository).findById(any(String.class));
        verify(orderService).updateOrderDetailsPartially(any(UUID.class), any(OrderDetailsDTO.class));
        verify(paymentRepository).save(any(Payment.class));
        verify(orderRepository).getReferenceById(any(UUID.class));
        verify(productRepository).saveAllAndFlush(anyIterable());

        expectedPayment.setPaymentStatus(PaymentStatus.FAILED);
        OrderDetails actual = orderRepository.findById(order.getId()).get();
        org.assertj.core.api.Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "customerOrdersById", "payment", "orderItems",
                        "billingAddress", "purchaseDate", "status")
                .isEqualTo(order);

        Assertions.assertEquals(Status.FAILED, actual.getStatus());
    }

}
