package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Payment;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    void addPayment(Payment payment);

    void deletePayment(String paymentId);

    void updatePayment(Payment payment);

    Payment getPayment(String id);

    void handlePaymentIntent(String eventType, PaymentIntent paymentIntent);
}
