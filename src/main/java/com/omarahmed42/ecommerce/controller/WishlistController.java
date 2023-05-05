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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@RequiredArgsConstructor
@Tags(@Tag(name = "Wishlist"))
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/products/{product-id}/wishlist")
    @Operation(summary = "Creates a new wishlist item using product id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Wishlist item already exists")
    })
    public ResponseEntity<Void> addWishlist(@PathVariable("product-id") UUID productId) {
        wishlistService.addWishlist(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{product-id}")
    @Operation(summary = "Deletes a wishlist item using product id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Wishlist not found")
    })
    public ResponseEntity<Void> deleteWishlist(@PathVariable("product-id") UUID productId) {
        wishlistService.deleteWishlist(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/customer/{customer-id}/wishlist/{product-id}")
    @Operation(summary = "Retrieves a wishlist item using product id and customer id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "409", description = "Wishlist not found")
    })
    public ResponseEntity<ProductResponse> getWishlistItem(@PathVariable("product-id") UUID productId,
            @PathVariable("customer-id") UUID customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId, productId));
    }
}