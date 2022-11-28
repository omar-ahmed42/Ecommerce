package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.BillingAddressNotFoundException;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.repository.BillingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BillingAddressServiceImpl implements BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;

    @Autowired
    public BillingAddressServiceImpl(BillingAddressRepository billingAddressRepository) {
        this.billingAddressRepository = billingAddressRepository;
    }

    @Override
    public void addBillingAddress(BillingAddress billingAddress) {
        billingAddressRepository.save(billingAddress);
    }

    @Override
    public void deleteBillingAddress(BillingAddress billingAddress) {
        billingAddressRepository
                .findById(billingAddress.getId())
                .ifPresentOrElse(
                        billingAddressRepository::delete,
                        () -> {
                            throw new BillingAddressNotFoundException("Billing address not found");
                        }
                );
    }

    @Override
    public void updateBillingAddress(BillingAddress billingAddress) {
        billingAddressRepository
                .findById(billingAddress.getId())
                .ifPresentOrElse(
                        present -> billingAddressRepository.save(billingAddress),
                        () -> {
                            throw new BillingAddressNotFoundException("Billing address not found");
                        }
                );
    }
}
