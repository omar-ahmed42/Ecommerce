package com.omarahmed42.ecommerce.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.omarahmed42.ecommerce.config.security.CustomUserDetails;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.model.User;

public class UserDetailsUtils {
    private static final String ROLE_PREFIX = "ROLE_";

    private UserDetailsUtils() {
    }

    public static User getAuthenticatedUser() {
        return isAuthenticated() ? ((CustomUserDetails) getPrincipal()).getUser() : null;
    }

    public static boolean hasGrantedAuthority(String authorityName) {
        return isAuthenticated() && ((CustomUserDetails) getPrincipal())
                .getAuthorities().contains(new SimpleGrantedAuthority(authorityName));
    }

    public static boolean hasRole(String roleName) {
        return isAuthenticated() && ((CustomUserDetails) getPrincipal()).getAuthorities()
                .contains(new SimpleGrantedAuthority(ROLE_PREFIX + roleName));
    }

    public static boolean hasRole(Role role) {
        return isAuthenticated() && ((CustomUserDetails) getPrincipal()).getAuthorities()
                .contains(new SimpleGrantedAuthority(ROLE_PREFIX + role.toString()));
    }

    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isAuthenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
