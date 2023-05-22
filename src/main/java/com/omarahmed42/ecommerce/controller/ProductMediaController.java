package com.omarahmed42.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.omarahmed42.ecommerce.DTO.AttachmentInfo;
import com.omarahmed42.ecommerce.service.ProductMediaService;
import com.omarahmed42.ecommerce.service.ProductMediaServiceFacade;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Hidden
public class ProductMediaController {

    private final ProductMediaService productMediaService;
    private final ProductMediaServiceFacade productMediaServiceFacade;

    @PostMapping("/products/{product-id}/media")
    public ResponseEntity<Void> addProductMedia(@PathVariable("product-id") UUID productId,
            @RequestParam("file") MultipartFile file) {
        productMediaServiceFacade.addProductMedia(productId, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{product-id}/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable("product-id") UUID productId,
            @RequestParam("id") UUID mediaId) {
        productMediaService.deleteProductMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{product-id}/media")
    public ResponseEntity<List<AttachmentInfo>> getProductMedia(@PathVariable("product-id") UUID productId) {
        return ResponseEntity.ok(productMediaService.getProductMediaByProductId(productId));
    }

    @GetMapping("/products/{product-id}/media/{media-id}")
    public ResponseEntity<AttachmentInfo> getProductMedia(@PathVariable("product-id") UUID productId,
            @PathVariable("media-id") UUID mediaId) {
        return ResponseEntity.ok(productMediaService.getProductMedia(mediaId));
    }
}
