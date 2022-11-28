package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.WishlistItemDTO;
import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.exception.WishlistNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import com.omarahmed42.ecommerce.service.WishlistService;
import com.omarahmed42.ecommerce.util.BigIntegerHandler;

@RestController
@RequestMapping("/v1")
public class WishlistController {
    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/customer/{customerId}/products/{productId}/wishlist")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<Object> addNewWishlist(@PathVariable("productId") BigInteger productId,
            @PathVariable("customerId") BigInteger customerIdPathVariable) {
        try {
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            wishlistService.addWishlist(new Wishlist(customerId, BigIntegerHandler.toByteArray(productId)));
            return ResponseEntity.ok().build();
        } catch (CustomerNotFoundException | ProductNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/customer/{customerId}/wishlist/{productId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> deleteWishlist(@PathVariable("productId") BigInteger productId,
            @PathVariable("customerId") BigInteger customerIdPathVariable) {
        try {
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            wishlistService.deleteWishlist(new WishlistPK(customerId, BigIntegerHandler.toByteArray(productId)));
            return ResponseEntity.noContent().build();
        } catch (WishlistNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/customer/{customerId}/wishlist/{productId}", produces = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || (principal.userId == #customerIdPathVariable)")
    public ResponseEntity<String> getWishlist(@PathVariable("productId") BigInteger productId,
            @PathVariable("customerId") BigInteger customerIdPathVariable) {
        try {
            byte[] customerId = BigIntegerHandler.toByteArray(customerIdPathVariable);
            Wishlist wishlist = wishlistService
                    .getWishlist(new WishlistPK(customerId, BigIntegerHandler.toByteArray(productId)));
            Product product = wishlist.getProductByProductId();
            WishlistItemDTO wishlistItemDTO = new WishlistItemDTO(productId, product.getName(),
                    product.getDescription());
            return ResponseEntity.ok().body(new Gson().toJson(wishlistItemDTO));
        } catch (WishlistNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}