package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.exception.VendorNotFoundException;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorServiceImpl(VendorRepository vendorRepository, UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void addVendor(Vendor vendor) {
        userRepository
                .findById(vendor.getUserId())
                .ifPresentOrElse(presentUser -> vendorRepository.save(vendor),
                        () -> {throw new UserNotFoundException("User not found");});
    }

    @Transactional
    @Override
    public void deleteVendor(UUID id) {
        vendorRepository
                .findById(id)
                .ifPresentOrElse(
                        vendorRepository::delete,
                        () -> {throw new VendorNotFoundException("Vendor not found");}
                );
    }

    @Transactional
    @Override
    public void updateVendor(Vendor vendor) {
        Vendor vendorById = vendorRepository
                .findById(vendor.getId())
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
        if (vendorById.getUserId() != vendor.getUserId() && userRepository.findById(vendorById.getUserId()).isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        vendorRepository.save(vendor);
    }
}
