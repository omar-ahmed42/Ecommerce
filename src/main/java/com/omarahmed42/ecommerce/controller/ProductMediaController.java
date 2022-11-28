package com.omarahmed42.ecommerce.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.omarahmed42.ecommerce.util.BigIntegerHandler;

@RestController
@RequestMapping("/v1")
public class ProductMediaController {
    private final ProductMediaService productMediaService;

    @Autowired
    public ProductMediaController(ProductMediaService productMediaService) {
        this.productMediaService = productMediaService;
    }

    @PostMapping("/{productId}/media")
    public ResponseEntity<String> addNewMedia(@PathVariable("productId") BigInteger productId, @RequestBody Set<String> mediaUrls) {
        try {
            List<ProductMedia> productMedia = new ArrayList<>(mediaUrls.size());
            byte[] idByteArray = BigIntegerHandler.toByteArray(productId);
            mediaUrls
                    .forEach(url ->
                            productMedia.add(new ProductMedia(idByteArray, url)));
            productMediaService.addProductMedia(productMedia);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/media")
    public ResponseEntity<String> deleteMedia(@RequestParam("id") Set<BigInteger> mediaProductIds) {
        try {
            List<ProductMedia> productMediaCollection = new ArrayList<>();
            mediaProductIds
                    .forEach(id -> {
                        ProductMedia productMedia = new ProductMedia();
                        productMedia.setId(BigIntegerHandler.toByteArray(id));
                        productMediaCollection.add(productMedia);
                    });
            productMediaService.deleteProductMedia(productMediaCollection);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
