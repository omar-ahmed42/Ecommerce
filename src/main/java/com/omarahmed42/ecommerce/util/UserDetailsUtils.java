package com.omarahmed42.ecommerce.util;

import org.springframework.security.core.userdetails.UserDetails;

import com.omarahmed42.ecommerce.config.security.AdminUserDetails;

public class UserDetailsUtils {

    private UserDetailsUtils() {
    }

    public static boolean nonAdmin(UserDetails authenticatedUser) {
        return !(authenticatedUser instanceof AdminUserDetails);
    }
}
