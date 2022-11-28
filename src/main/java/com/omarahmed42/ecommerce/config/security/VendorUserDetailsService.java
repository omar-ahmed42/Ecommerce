package com.omarahmed42.ecommerce.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.exception.VendorNotVerifiedException;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;

public class VendorUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VendorRepository vendorRepository;

    public VendorUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.omarahmed42.ecommerce.model.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Vendor vendor = vendorRepository
                .findById(user.getId())
                .orElseGet(() -> vendorRepository.save(new Vendor(user.getId())));

        if (!vendor.isVerifiedVendor()) {
            throw new VendorNotVerifiedException("Vendor not verified");
        }
        return new VendorUserDetails(user, vendor);
    }
}
