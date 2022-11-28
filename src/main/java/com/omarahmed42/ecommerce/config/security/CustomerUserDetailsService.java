package com.omarahmed42.ecommerce.config.security;

import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.omarahmed42.ecommerce.model.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Customer customer = customerRepository
                .findById(user.getId())
                .orElseGet(() -> customerRepository.save(new Customer(user.getId())));

        return new CustomerUserDetails(user, customer);
    }
}
