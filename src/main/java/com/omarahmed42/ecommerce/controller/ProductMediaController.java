package com.omarahmed42.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.model.ProductMedia;
import com.omarahmed42.ecommerce.service.ProductMediaService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/v1")
@Hidden
public class ProductMediaController {
    private final ProductMediaService productMediaService;

    public ProductMediaController(ProductMediaService productMediaService) {
        this.productMediaService = productMediaService;
    }

    @PostMapping("/products/{product-id}/media")
    public ResponseEntity<String> addNewMedia(@PathVariable("product-id") UUID productId,
            @RequestBody Set<String> mediaUrls) {
        List<ProductMedia> productMedia = new ArrayList<>(mediaUrls.size());
        // mediaUrls
        // .forEach(url ->
        // productMedia.add(new ProductMedia(productId, url)));
        productMediaService.addProductMedia(productMedia);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/media")
    public ResponseEntity<String> deleteMedia(@RequestParam("id") Set<UUID> mediaProductIds) {
        List<ProductMedia> productMediaCollection = new ArrayList<>();
        mediaProductIds
                .forEach(id -> {
                    ProductMedia productMedia = new ProductMedia();
                    productMedia.setId(id);
                    productMediaCollection.add(productMedia);
                });
        productMediaService.deleteProductMedia(productMediaCollection);
        return ResponseEntity.noContent().build();
    }
}
