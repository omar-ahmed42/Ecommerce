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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@Tags(@Tag(name = "Product"))
@RequiredArgsConstructor
public class ProductController {
        // TODO Implement Search
        private final ProductService productService;

        // Implement pagination, sorting and filtering (Use ElasticSearch)
        // TODO: handle media saving (media storage, uploading)
        // Upload images
        @PostMapping("/vendor/{vendor-id}/products")
        @Operation(summary = "Creates a new product")
        @ApiResponse(responseCode = "201", description = "Created")
        public ResponseEntity<Void> addProduct(@RequestBody ProductRequest product,
                        @PathVariable(name = "vendorId") UUID vendorId) {
                UUID id = productService.addProduct(vendorId, product).getId();
                return ResponseEntity.created(URI.create("/products/" + id.toString())).build();
        }

        @DeleteMapping("/products/{product-id}")
        @Operation(summary = "Deletes a product by id")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to delete a product"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<Void> deleteProduct(
                        @Parameter(name = "product-id", description = "Product id") @PathVariable("product-id") UUID productId) {
                productService.deleteProduct(productId);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/products/{product-id}")
        @Operation(summary = "Updates a product by id")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "No Content"),
                        @ApiResponse(responseCode = "403", description = "Access Denied"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized user with id ${ID} tried to update a product"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductRequest> updateProduct(@PathVariable(name = "product-id") UUID productId,
                        @RequestBody ProductRequest productRequest) {
                productService.updateProduct(productId, productRequest);
                return ResponseEntity.noContent().build();
        }

        @GetMapping(value = "/products/{product-id}")
        @Operation(summary = "Retrieves a product by id")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductResponse> getProduct(
                        @Parameter(name = "product-id", description = "Product id") @PathVariable(name = "product-id") UUID id) {
                return ResponseEntity.ok(productService
                                .getProduct(id));
        }
}