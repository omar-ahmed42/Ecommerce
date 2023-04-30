package com.omarahmed42.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonSyntaxException;
import com.omarahmed42.ecommerce.service.PaymentService;
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

    private final PaymentService paymentService;

    public OrderFulfillmentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
        paymentService.handlePaymentIntent(event.getType(), (PaymentIntent) stripeObject);
        return ResponseEntity.ok().build();
    }
}
