package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Vendor;

public interface VendorService {
    void addVendor(Vendor vendor);
    void deleteVendor(byte[] id);
    void updateVendor(Vendor vendor);
}
