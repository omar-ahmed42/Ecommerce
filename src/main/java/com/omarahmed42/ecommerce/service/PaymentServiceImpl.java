package com.omarahmed42.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.enums.PaymentStatus;
import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.exception.PaymentNotFoundException;
import com.omarahmed42.ecommerce.model.OrderDetails;
import com.omarahmed42.ecommerce.model.Payment;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.OrderItem;
import com.omarahmed42.ecommerce.repository.OrderDetailsRepository;
import com.omarahmed42.ecommerce.repository.PaymentRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final OrderDetailsService ordersService;
    private final OrderDetailsRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    private static final String ORDER_ID = "orderId";

    @Override
    @Transactional
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void deletePayment(String paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    @Override
    @Transactional
    public void updatePayment(Payment payment) {
        paymentRepository
                .findById(payment.getPaymentIntentId())
                .ifPresentOrElse(
                        presentPayment -> paymentRepository.save(payment),
                        PaymentNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPayment(String id) {
        return paymentRepository
                .findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    @Transactional
    public void handlePaymentIntent(String eventType, PaymentIntent paymentIntent) {
        switch (eventType) {
            case "payment_intent.created":
                logger.info("Payment for {} created.", paymentIntent.getAmount());
                handlePaymentCreated(paymentIntent);
                break;
            case "payment_intent.succeeded":
                logger.info("Payment for {} succeeded.", paymentIntent.getAmount());
                handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_intent.payment_failed":
                logger.info("Payment for {} failed.", paymentIntent.getId());
                handlePaymentIntentFailed(paymentIntent);
                break;
            default:
                logger.warn("Unhandled event type: {}", eventType);
                break;
        }
    }

    private void handlePaymentCreated(PaymentIntent paymentIntent) {
        if (paymentRepository.existsById(paymentIntent.getId()))
            return;

        Map<String, String> metadata = paymentIntent.getMetadata();
        UUID orderId = UUID.fromString(metadata.get(ORDER_ID));
        Payment payment = new Payment(paymentIntent.getId(), paymentIntent.getAmount(), PaymentStatus.CREATED, orderRepository.getReferenceById(orderId));
        paymentRepository.save(payment);
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        Optional<Payment> retrievedPayment = paymentRepository.findById(paymentIntent.getId());
        if (retrievedPayment.isEmpty()) {
            return;
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        UUID orderId = UUID.fromString(metadata.get(ORDER_ID));
        ordersService
                .updateOrderDetailsPartially(orderId, OrderDetailsDTO
                        .builder()
                        .status(Status.COMPLETED)
                        .build());
        Payment payment = retrievedPayment.get();
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        Optional<Payment> retrievedPayment = paymentRepository.findById(paymentIntent.getId());
        if (retrievedPayment.isEmpty()) {
            return;
        }
        Map<String, String> metadata = paymentIntent.getMetadata();
        UUID orderId = UUID.fromString(metadata.get(ORDER_ID));
        ordersService
                .updateOrderDetailsPartially(orderId, OrderDetailsDTO
                        .builder()
                        .status(Status.FAILED)
                        .build());

        Payment payment = retrievedPayment.get();
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        updateProductStock(orderId);
    }

    private void updateProductStock(UUID orderId) {
        OrderDetails order = orderRepository.getReferenceById(orderId);
        List<OrderItem> productItemsById = order.getOrderItems();
        List<Product> products = new ArrayList<>(productItemsById.size());
        for (OrderItem productItem : productItemsById) {
            Product product = productItem.getProduct();
            int stock = product.getStock() + productItem.getQuantity();
            product.setStock(stock);
            products.add(product);
        }

        productRepository.saveAllAndFlush(products);
    }
}
