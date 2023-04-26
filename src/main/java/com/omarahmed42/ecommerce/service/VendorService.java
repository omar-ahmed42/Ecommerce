package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.Vendor;

public interface VendorService {
    void addVendor(Vendor vendor);
    void deleteVendor(UUID id);
    void updateVendor(Vendor vendor);
}
