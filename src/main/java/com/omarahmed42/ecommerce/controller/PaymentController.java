package com.omarahmed42.ecommerce.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.CreatePayment;
import com.omarahmed42.ecommerce.DTO.CreatePaymentResponse;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.service.OrdersService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
public class PaymentController {

    private final OrdersService ordersService;
    private static ModelMapper modelMapper = new ModelMapper();

    public PaymentController(OrdersService ordersService) {
        this.ordersService = ordersService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/create-payment-intent")
    public ResponseEntity<CreatePaymentResponse> createPaymentIntent(@RequestBody CreatePayment createPayment)
            throws StripeException {
        CartItemDTO[] cartItems = createPayment.getItems();
        if (cartItems.length == 0)
            return ResponseEntity.ok().build();
        Orders order = ordersService.addOrder(createPayment.getUserId(), cartItems, createPayment.getBillingAddress());
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(order.getTotalPrice().longValueExact() * 100L)
                .setCurrency("usd")
                .putMetadata("orderId", order.getId().toString())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build())
                .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret());
        return ResponseEntity.ok(paymentResponse);
    }

}