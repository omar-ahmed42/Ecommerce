package com.omarahmed42.ecommerce.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;

public class VendorUserDetails implements UserDetails, UserDetailsId {
    private User user;
    private Vendor vendor;

    public VendorUserDetails() {
    }

    public VendorUserDetails(User user, Vendor vendor) {
        this.user = user;
        this.vendor = vendor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(vendor.isVerifiedVendor() ? "ROLE_" + Role.VERIFIED_VENDOR.toString() : "ROLE_" + Role.UNVERIFIED_VENDOR.toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
    
    public UUID getUserId(){
        return this.user.getId();
    }
}
