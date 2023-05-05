package com.omarahmed42.ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.omarahmed42.ecommerce.enums.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(daoAuthenticationProvider());
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/documentation/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/categories").hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.PUT, "/v1/categories/{id}").hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/categories/{id}").hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.GET, "/v1/categories/{id}").permitAll()

                .antMatchers(HttpMethod.POST, "/v1/carts").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.PUT, "/v1/carts/{product-id}").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/carts/{product-id}").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET, "/v1/customers/{customer-id}/carts/{product-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())

                .antMatchers(HttpMethod.POST, "/v1/products/{product-id}/categories").hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/categories/{category-id}/products/{product-id}")
                .hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.GET, "/v1/products/{product-id}/categories").permitAll()

                .antMatchers(HttpMethod.GET, "/v1/confirm").permitAll()

                .antMatchers(HttpMethod.POST, "/v1/users").access("hasRole(Role.ADMIN.toString()) || isAnonymous()")
                .antMatchers(HttpMethod.GET, "/v1/users/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/v1/users/{id}").hasRole(Role.ADMIN.toString())
                .antMatchers(HttpMethod.PATCH, "/v1/users/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/v1/vendor/{vendor-id}/products")
                .hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/products/{product-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .antMatchers(HttpMethod.PUT, "/v1/products/{product-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.VERIFIED_VENDOR.toString())
                .antMatchers(HttpMethod.GET, "/v1/products/{product-id}").permitAll()

                .antMatchers(HttpMethod.POST, "/v1/products/{product-id}/wishlist").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/wishlist/{product-id}").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET, "/v1/customer/{customer-id}/wishlist/{product-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.POST, "/v1/products/{product-id}/reviews").hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/products/{product-id}/reviews/{review-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.PUT, "/v1/products/{product-id}/reviews/{review-id}")
                .hasAnyRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.POST, "/v1/reviews/{review-id}/comments").hasAnyRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.DELETE, "/v1/reviews/{review-id}/comments/{comment-id}")
                .hasAnyRole(Role.ADMIN.toString(), Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.PUT, "/v1/reviews/{review-id}/comments/{comment-id}")
                .hasRole(Role.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET, "/v1/reviews/{review-id}/comments/{comment-id}").permitAll()

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .loginProcessingUrl("/v1/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/v1/logout")
                .deleteCookies("JSESSIONID");
        return http.build();

    }
}
