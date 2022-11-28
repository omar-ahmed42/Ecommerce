package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.omarahmed42.ecommerce.util.BigIntegerHandler;

@RestController
@RequestMapping("/v1")
public class CartController {
    private final CartService cartService;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/customer/{customerId}/cart")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> addNewCartItemToCart(@RequestBody CartItemDTO cartItemDTO,
            @PathVariable(name = "customerId") BigInteger customerIdPathVariable) {
        try {
            Cartitems cartItem = modelMapper.map(cartItemDTO, Cartitems.class);
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            cartItem.setCustomerId(customerId);
            cartItem.setProductId(BigIntegerHandler.toByteArray(cartItemDTO.getProductId()));
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
    public ResponseEntity<String> updateCartItem(@PathVariable BigInteger productId,
            @RequestBody CartItemDTO cartItemDTO,
            @PathVariable(name = "customerId") BigInteger customerIdPathVariable) {
        try {
            Cartitems cartItem = modelMapper.map(cartItemDTO, Cartitems.class);
            byte[] productIdBytes = BigIntegerHandler.toByteArray(productId);
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            cartItem.setCustomerId(customerId);
            cartItem.setProductId(productIdBytes);
            if (cartItem.getQuantity() == 0) {
                cartService.deleteCartItem(cartItem);
            }
            cartService.updateCartItem(new CartitemsPK(customerId, productIdBytes), cartItem);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/customer/{customerId}/cart/items/{productId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> deleteCartItem(@PathVariable(name = "productId") BigInteger productId,
            @PathVariable("customerId") BigInteger customerIdPathVariable) {
        try {
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            cartService.deleteCartItem(new Cartitems(customerId, BigIntegerHandler.toByteArray(productId)));
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
