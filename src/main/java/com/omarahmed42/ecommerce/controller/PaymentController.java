package com.omarahmed42.ecommerce.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.CreatePayment;
import com.omarahmed42.ecommerce.DTO.CreatePaymentResponse;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.ProductItem;
import com.omarahmed42.ecommerce.service.OrdersService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("/v1")
public class PaymentController {

    private static Gson gson = new Gson();
    private final OrdersService ordersService;
    private static ModelMapper modelMapper = new ModelMapper();

    public PaymentController(OrdersService ordersService) {
        this.ordersService = ordersService;
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @PostMapping(value = "/customer/create-payment-intent", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || principal.userId == #createPayment.userId")
    public ResponseEntity<String> createPaymentIntent(@RequestBody CreatePayment createPayment)
            throws StripeException {
        try {
            CartItemDTO[] cartItems = createPayment.getItems();
            int numberOfItems = cartItems.length;
            ProductItem[] productItems = new ProductItem[numberOfItems];
            for (int i = 0; i < numberOfItems; i++) {
                productItems[i] = new ProductItem(cartItems[i].getProductId(), cartItems[i].getPrice(),
                        cartItems[i].getQuantity());
            }
            BillingAddress billingAddress = modelMapper.map(createPayment.getBillingAddress(), BillingAddress.class);
            Orders order = ordersService.addNewOrders(createPayment.getUserId(), productItems, billingAddress);
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
            return ResponseEntity.ok(gson.toJson(paymentResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}