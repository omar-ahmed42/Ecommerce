package com.omarahmed42.ecommerce.config.security;

import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;

public class CustomerUserDetails implements UserDetails, UserDetailsId {
    private User user;
    private Customer customer;
    private BigInteger userId;

    public CustomerUserDetails() {
    }

    public CustomerUserDetails(User user, Customer customer) {
        this.user = user;
        this.customer = customer;
        this.userId = new BigInteger(this.user.getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + Role.CUSTOMER.toString()));
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

    public BigInteger getUserId(){
        return this.userId;
    }
}
