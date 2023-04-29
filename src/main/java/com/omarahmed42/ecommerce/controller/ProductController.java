package com.omarahmed42.ecommerce.controller;

import java.net.URI;
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

import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@RequiredArgsConstructor
public class ProductController {
    // TODO Implement Search
    private final ProductService productService;

    // Implement pagination, sorting and filtering (Use ElasticSearch)
    // TODO: handle media saving (media storage, uploading)
    // Upload images
    @PostMapping("/vendor/{vendor-id}/products")
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequest productDTO,
            @PathVariable(name = "vendorId") UUID vendorId) {
        UUID id = productService.addProduct(vendorId, productDTO).getId();
        return ResponseEntity.created(URI.create("/products/" + id.toString())).build();
    }

    @DeleteMapping("/products/{product-id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("product-id") UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{product-id}")
    public ResponseEntity<ProductRequest> updateProduct(@PathVariable(name = "product-id") UUID productId,
            @RequestBody ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/products/{product-id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "product-id") UUID id) {
        return ResponseEntity.ok(productService
                .getProduct(id));
    }
}
