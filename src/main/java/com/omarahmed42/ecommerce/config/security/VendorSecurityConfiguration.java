package com.omarahmed42.ecommerce.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.omarahmed42.ecommerce.enums.Role;

@Configuration
@Order(1)
public class VendorSecurityConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean(name = "VendorUserDetailsServiceBean")
    public UserDetailsService vendorUserDetailsService() {
        return new VendorUserDetailsService();
    }

    @Bean(name = "VendorAuthenticationProvider")
    public DaoAuthenticationProvider vendorAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(vendorUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean(name = "VendorFilterChain")
    public SecurityFilterChain vendorFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(vendorAuthenticationProvider());
        http
        .csrf().disable()
        .antMatcher("/v*/vendor/**")
                .authorizeHttpRequests()
                .antMatchers("/vendor/login").permitAll()
                .antMatchers(HttpMethod.DELETE, "/v*/vendor/{vendorId}/products/{productId}").hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .antMatchers(HttpMethod.PUT, "/v*/vendor/{vendorId}/products/{productId}").hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .antMatchers(HttpMethod.POST, "/v*/vendor/{vendorId}/product").hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .and()
                .formLogin()
                    .loginPage("/vendor/login")
                    .usernameParameter("email")
                    .loginProcessingUrl("/v1/vendor/login")
                    .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/v1/vendor/logout")
                .deleteCookies("JSESSIONID");
        return http.build();
    }
}
