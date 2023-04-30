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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<Void> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        cartService.addCartItem(cartItemDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/carts/{product-id}")
    public ResponseEntity<Void> updateCartItem(@PathVariable UUID productId,
            @RequestBody CartItemDTO cartItemDTO) {
        cartService.updateCartItem(productId, cartItemDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/carts/{product-id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable(name = "product-id") UUID productId) {
        cartService.deleteCartItem(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/{customer-id}/carts/{product-id}")
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable("customer-id") UUID customerId,
            @PathVariable("product-id") UUID productId) {
        return ResponseEntity.ok(cartService.getCartItem(customerId, productId));
    }
}
