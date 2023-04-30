package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/products/{product-id}/wishlist")
    public ResponseEntity<Void> addWishlist(@PathVariable("product-id") UUID productId) {
        wishlistService.addWishlist(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{product-id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable("product-id") UUID productId) {
        wishlistService.deleteWishlist(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/customer/{customer-id}/wishlist/{product-id}", produces = "application/json")
    public ResponseEntity<ProductResponse> getWishlistItem(@PathVariable("product-id") UUID productId,
            @PathVariable("customer-id") UUID customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId, productId));
    }
}