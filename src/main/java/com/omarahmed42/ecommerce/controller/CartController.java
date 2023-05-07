package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Tags(@Tag(name = "Cart"))
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/carts")
    @Operation(summary = "Creates a new cart item")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Requested product exceeds the available stock products")
    @ApiResponse(responseCode = "403", description = "Access Denied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "422", description = "Cart item is missing")
    @ApiResponse(responseCode = "422", description = "Quantity is missing")
    @ApiResponse(responseCode = "422", description = "Price is missing")
    @ApiResponse(responseCode = "422", description = "Price cannot be less than 0")
    public ResponseEntity<Void> addCartItem(@Parameter(name = "cart item") @RequestBody CartItemDTO cartItemDTO) {
        cartService.addCartItem(cartItemDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(value ="/carts/{product-id}")
    @Operation(summary = "Updates a cart item")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "400", description = "Requested product exceeds the available stock products")
    @ApiResponse(responseCode = "403", description = "Access Denied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "422", description = "Cart item is missing")
    @ApiResponse(responseCode = "422", description = "Quantity is missing")
    @ApiResponse(responseCode = "422", description = "Price is missing")
    @ApiResponse(responseCode = "422", description = "Price cannot be less than 0")
    public ResponseEntity<Void> updateCartItem(@PathVariable(name = "product-id") UUID productId,
            @Parameter(name = "cart item") @RequestBody CartItemDTO cartItemDTO) {
        cartService.updateCartItem(productId, cartItemDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/carts/{product-id}", consumes = "*/*")
    @Operation(summary = "Deletes a cart item")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "403", description = "Access Denied")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    public ResponseEntity<Void> deleteCartItem(@PathVariable(name = "product-id") UUID productId) {
        cartService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/customers/{customer-id}/carts/{product-id}", consumes = "*/*")
    @Operation(summary = "Retrieves a cart item")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "403", description = "Access Denied")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable("customer-id") UUID customerId,
            @PathVariable("product-id") UUID productId) {
        return ResponseEntity.ok(cartService.getCartItem(customerId, productId));
    }
}
