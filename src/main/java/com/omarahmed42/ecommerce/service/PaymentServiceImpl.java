package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.PaymentNotFoundException;
import com.omarahmed42.ecommerce.model.Payment;
import com.omarahmed42.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void deletePayment(String paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    @Transactional
    @Override
    public void updatePayment(Payment payment) {
        paymentRepository
                .findById(payment.getPaymentIntentId())
                .ifPresentOrElse(
                        presentPayment -> paymentRepository.save(payment)
                        ,
                        () -> {
                            throw new PaymentNotFoundException("Payment not found");
                        }
                );
    }

    @Transactional
    @Override
    public Payment getPayment(String id) {
        return paymentRepository
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    public Optional<Payment> findPayment(String id){
        return paymentRepository
                .findById(id);
    }
}
