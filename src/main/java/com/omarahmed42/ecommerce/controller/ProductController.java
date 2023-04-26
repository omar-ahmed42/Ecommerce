package com.omarahmed42.ecommerce.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.config.security.AdminUserDetails;
import com.omarahmed42.ecommerce.config.security.UserDetailsId;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductMedia;
import com.omarahmed42.ecommerce.service.ProductMediaService;
import com.omarahmed42.ecommerce.service.ProductService;

@RestController
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
public class ProductController {
    // TODO Implement Search
    private final ProductService productService;
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    private final ProductMediaService productMediaService;
    private final ModelMapper modelMapper = new ModelMapper();

    public ProductController(ProductService productService, ProductMediaService productMediaService) {
        this.productService = productService;
        this.productMediaService = productMediaService;
    }

    // TODO: handle media saving (media storage, uploading)
    // Upload images
    @PostMapping("/vendor/{vendor-id}/product")
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequest productDTO,
            @PathVariable(name = "vendorId") UUID vendorId) {
        try {
            UUID id = productService.addProduct(vendorId, productDTO).getId();

            if (Objects.nonNull(productDTO.getMediaURLs()) && !productDTO.getMediaURLs().isEmpty()) {
                List<ProductMedia> productMediaCollection = createProductMedias(id, productDTO.getMediaURLs());
                productMediaService.addProductMedia(productMediaCollection);
            }
            return ResponseEntity.created(URI.create("/products/" + id.toString())).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<ProductMedia> createProductMedias(UUID productId, Set<String> mediaUrls) {
        List<ProductMedia> productMediaCollection = new ArrayList<>(mediaUrls.size());
        for (String url : mediaUrls) {
            productMediaCollection.add(new ProductMedia(productId, url));
        }
        return productMediaCollection;
    }

    @DeleteMapping("/vendor/{vendor-id}/products/{product-id}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || principal.userId == #vendorId")
    public ResponseEntity<Void> deleteProduct(@PathVariable("product-id") UUID id,
            @PathVariable("vendorId") UUID vendorId, @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            UUID productId = id;
            if (nonAdmin(authenticatedUser)
                    && !Objects.equals(productService.getProductById(productId).getVendorId(), vendorId)) {
                logger.info("Unauthorized user: %d attempted to delete a resource",
                        ((UserDetailsId) authenticatedUser).getUserId());
                return ResponseEntity.status(401).build();
            }
            productService.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException productNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean nonAdmin(UserDetails authentedUser) {
        return !(authentedUser instanceof AdminUserDetails);
    }

    @PutMapping("/vendor/{vendor-id}/products/{product-id}")
    @PreAuthorize("hasRole(Role.ADMIN.toString()) || principal.userId == #vendorId")
    public ResponseEntity<ProductRequest> updateProduct(@PathVariable(name = "product-id") UUID productId,
            @RequestBody ProductRequest productDTO,
            @PathVariable("vendorId") UUID vendorId, @AuthenticationPrincipal UserDetails authenticatedUser) {
        try {
            Product product = modelMapper.map(productDTO, Product.class);
            /*
             * TODO: This method could be optimized even more by implementing another update
             * method that takes 2 parameters
             * first: newEntity
             * second: retrievedEntity (to prevent hitting the database twice just to
             * retrieve the entity, once in IDOR prevention and once in the current
             * updateProduct)
             */
            if (nonAdmin(authenticatedUser) &&
                    !Objects.equals(productService.getProductById(productId).getVendorId(), vendorId)) {
                logger.info("Unauthorized user: %d attempted to update a resource",
                        ((UserDetailsId) authenticatedUser).getUserId());
                return ResponseEntity.status(401).build();
            }
            product.setId((productId));
            productService.updateProduct(product);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException productNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/products/{product-id}", produces = "application/json")
    public ResponseEntity<String> getProduct(@PathVariable(name = "product-id") UUID id) {
        try {
            Product product = productService
                    .getProductById(id);
            ProductRequest productDTO = modelMapper.map(product, ProductRequest.class);

            return ResponseEntity.ok(new Gson().toJson(productDTO));
        } catch (ProductNotFoundException productNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
