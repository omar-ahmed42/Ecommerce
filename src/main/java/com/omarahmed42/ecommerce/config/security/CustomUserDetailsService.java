package com.omarahmed42.ecommerce.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omarahmed42.ecommerce.exception.BadUsernameException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(BadUsernameException::new);
        return new CustomUserDetails(user);
    }
}
