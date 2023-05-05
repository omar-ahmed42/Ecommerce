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
import com.omarahmed42.ecommerce.model.OrderDetails;
import com.omarahmed42.ecommerce.service.OrderDetailsService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@Tags(value = { @Tag(name = "Payment") })
public class PaymentController {

        private final OrderDetailsService ordersService;
        private static ModelMapper modelMapper = new ModelMapper();

        public PaymentController(OrderDetailsService ordersService) {
                this.ordersService = ordersService;
                modelMapper.getConfiguration().setSkipNullEnabled(true);
        }

        @PostMapping(value = "/create-payment-intent")
        @Operation(summary = "Creates a payment intent in order to process an order (checkout)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "422", description = "Products ids are missing"),
                        @ApiResponse(responseCode = "422", description = "More than stock capacity"),
        })
        public ResponseEntity<CreatePaymentResponse> createPaymentIntent(
                        @RequestBody @Parameter(description = "Contains info about the payment such as user id, billing address and items") CreatePayment createPayment)
                        throws StripeException {
                CartItemDTO[] cartItems = createPayment.getItems();
                if (cartItems.length == 0)
                        return ResponseEntity.ok().build();
                OrderDetails order = ordersService.addOrderDetails(createPayment.getUserId(), cartItems,
                                createPayment.getBillingAddress());
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