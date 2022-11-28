package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonSyntaxException;
import com.omarahmed42.ecommerce.enums.PaymentStatus;
import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.Payment;
import com.omarahmed42.ecommerce.model.ProductItem;
import com.omarahmed42.ecommerce.service.OrdersService;
import com.omarahmed42.ecommerce.service.PaymentService;
import com.omarahmed42.ecommerce.service.ProductService;
import com.omarahmed42.ecommerce.util.BigIntegerHandler;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/v1")
public class OrderFulfillmentController {

    private static final Logger logger = LoggerFactory.getLogger(OrderFulfillmentController.class);
    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    private final OrdersService ordersService;
    private final PaymentService paymentService;

    private final ProductService productService;

    @Autowired
    public OrderFulfillmentController(OrdersService ordersService, PaymentService paymentService,
            ProductService productService) {
        this.ordersService = ordersService;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @PostMapping("/order-webhook")
    public ResponseEntity<String> fulfillOrder(@RequestHeader(name = "Stripe-Signature") String sigHeader,
            @RequestBody String payload) {
        Event event;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            logger.error("⚠️  Webhook error while parsing basic request.");
            return ResponseEntity.status(400).build();
        }

        if (endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise, use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                // Invalid signature
                logger.error("⚠️  Webhook error while validating signature.");
                return ResponseEntity.status(400).build();
            }
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            logger.error("Unable to deserialize event data object for {}", event);
            return ResponseEntity.status(400).build();
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.created":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment for {} created.", paymentIntent.getAmount());
                handlePaymentCreated(paymentIntent);
                break;
            case "payment_intent.succeeded":
                paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment for {} succeeded.", paymentIntent.getAmount());
                handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_intent.payment_failed":
                paymentIntent = (PaymentIntent) stripeObject;
                handlePaymentIntentFailed(paymentIntent);
                break;
            default:
                logger.warn("Unhandled event type: {}", event.getType());
                break;
        }
        return ResponseEntity.ok().build();
    }

    private void handlePaymentCreated(PaymentIntent paymentIntent) {
        if (paymentService.findPayment(paymentIntent.getId()).isPresent()) {
            return;
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        byte[] orderId = BigIntegerHandler.toByteArray(new BigInteger(metadata.get("orderId")));
        Payment payment = new Payment(paymentIntent.getId(), paymentIntent.getAmount(), PaymentStatus.CREATED, orderId);
        paymentService.addPayment(payment);
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        Optional<Payment> retrievedPayment = paymentService.findPayment(paymentIntent.getId());
        if (retrievedPayment.isEmpty()) {
            return;
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        byte[] orderId = BigIntegerHandler.toByteArray(new BigInteger(metadata.get("orderId")));
        Orders order = ordersService.getOrder(orderId);
        order.setStatus(Status.COMPLETED);
        ordersService.updateOrder(order);
        Payment payment = retrievedPayment.get();
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentService.addPayment(payment);
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        Optional<Payment> retrievedPayment = paymentService.findPayment(paymentIntent.getId());
        if (retrievedPayment.isEmpty()) {
            return;
        }
        Map<String, String> metadata = paymentIntent.getMetadata();
        byte[] orderId = BigIntegerHandler.toByteArray(new BigInteger(metadata.get("orderId")));
        Orders order = ordersService.getOrder(orderId);
        order.setStatus(Status.FAILED);
        ordersService.updateOrder(order);

        Payment payment = retrievedPayment.get();
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentService.addPayment(payment);
        new Thread(() -> updateProductStock(order)).start();
    }

    private void updateProductStock(Orders order) {
        List<ProductItem> productItemsById = order.getProductItemsById();
        List<com.omarahmed42.ecommerce.model.Product> products = new ArrayList<>(productItemsById.size());
        for (ProductItem productItem : productItemsById) {
            com.omarahmed42.ecommerce.model.Product product = productItem.getProductByProductId();
            int stock = product.getStock() + productItem.getQuantity();
            product.setStock(stock);
            products.add(product);
        }
        productService.updateProducts(products);
    }

}
