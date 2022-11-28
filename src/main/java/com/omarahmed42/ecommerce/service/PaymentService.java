package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Payment;

import java.util.Optional;

public interface PaymentService {
    void addPayment(Payment payment);
    void deletePayment(String paymentId);
    void updatePayment(Payment payment);
    Payment getPayment(String id);
    Optional<Payment> findPayment(String id);
}
