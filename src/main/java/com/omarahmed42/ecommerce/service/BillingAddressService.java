package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.BillingAddress;

public interface BillingAddressService {
    void addBillingAddress(BillingAddress billingAddress);
    void deleteBillingAddress(BillingAddress billingAddress);
    void updateBillingAddress(BillingAddress billingAddress);
}
