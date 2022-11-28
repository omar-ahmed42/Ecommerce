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
@Order(2)
public class CustomerSecurityConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean(name = "CustomerUserDetailsServiceBean")
    public UserDetailsService customerUserDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean(name = "CustomerAuthenticationProvider")
    public DaoAuthenticationProvider customerAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean(name = "CustomerFilterChain")
    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(customerAuthenticationProvider());
        http
        .csrf().disable()
        .antMatcher("/v*/customer/**")
                .authorizeHttpRequests()
                .antMatchers("/customer/login").permitAll()
                .antMatchers(HttpMethod.GET, "/checkout").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET, "/v*/customer/{customerId}/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v*/customer/{customerId}/**").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v*/customer/{customerId}/**").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.PUT, "/v*/customer/{customerId}/**").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers("/v*/confirm*").permitAll() // confirm account
                .antMatchers("/v*/orders/items/{orderItemId}").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers("/v*/customer/create-payment-intent").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v*/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments/{commentId}").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.POST, "/v*/customer/{customerId}/products/{productId}/reviews/{reviewId}/comments").hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .and()
                .formLogin()
                    .loginPage("/customer/login")
                    .usernameParameter("email")
                    .loginProcessingUrl("/v1/customer/login")
                    .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/v1/customer/logout")
                .deleteCookies("JSESSIONID");
        return http.build();
    }
}
