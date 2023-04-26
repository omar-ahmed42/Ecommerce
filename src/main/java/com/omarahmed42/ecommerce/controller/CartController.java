package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.exception.CartItemAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.CartItemNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;
import com.omarahmed42.ecommerce.service.CartService;

@RestController
@RequestMapping("/v1")
public class CartController {
    private final CartService cartService;
    private ModelMapper modelMapper = new ModelMapper();

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/customer/{customerId}/cart")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> addNewCartItemToCart(@RequestBody CartItemDTO cartItemDTO,
            @PathVariable(name = "customerId") UUID customerIdPathVariable) {
        try {
            Cartitems cartItem = modelMapper.map(cartItemDTO, Cartitems.class);
            UUID customerId = customerIdPathVariable;
            cartItem.setCustomerId(customerId);
            cartItem.setProductId(cartItemDTO.getProductId());
            cartService.addCartItem(cartItem);
            return ResponseEntity.status(201).build();
        } catch (CartItemAlreadyExistsException cartItemAlreadyExistsException) {
            cartItemAlreadyExistsException.printStackTrace();
            return ResponseEntity.status(303).build();
        } catch (ProductNotFoundException productNotFoundException) {
            productNotFoundException.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/customer/{customerId}/cart/items/{productId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> updateCartItem(@PathVariable UUID productId,
            @RequestBody CartItemDTO cartItemDTO,
            @PathVariable(name = "customerId") UUID customerId) {
        try {
            Cartitems cartItem = modelMapper.map(cartItemDTO, Cartitems.class);
            cartItem.setCustomerId(customerId);
            cartItem.setProductId(productId);
            if (cartItem.getQuantity() == 0) {
                cartService.deleteCartItem(cartItem);
            }
            cartService.updateCartItem(new CartitemsPK(customerId, productId), cartItem);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/customer/{customerId}/cart/items/{productId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> deleteCartItem(@PathVariable(name = "productId") UUID productId,
            @PathVariable("customerId") UUID customerId) {
        try {
            cartService.deleteCartItem(new Cartitems(customerId, productId));
            return ResponseEntity.noContent().build();
        } catch (CartItemNotFoundException cartItemNotFoundException) {
            cartItemNotFoundException.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
