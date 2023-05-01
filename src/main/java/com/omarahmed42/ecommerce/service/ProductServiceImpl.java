package com.omarahmed42.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductMedia;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;
import com.omarahmed42.ecommerce.validation.ProductValidation;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final ProductMediaService productMediaService;
    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, VendorRepository vendorRepository,
            ProductMediaService productMediaService) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.productMediaService = productMediaService;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || ((principal.user.id == #vendorId) && hasRole('VERIFIED_VENDOR'))")
    public ProductResponse addProduct(UUID vendorId, ProductRequest productRequest) {
        ProductValidation.validateProduct(productRequest);
        Product product = modelMapper.map(productRequest, Product.class);
        // Vendor vendor = vendorRepository.getReferenceById(vendorId);
        product.setVendorId(vendorId);
        // product.setVendorByVendorId(vendor);
        product = productRepository.save(product);

        addProductMedia(product.getId(), productRequest);

        return modelMapper.map(product, ProductResponse.class);
    }

    private void addProductMedia(UUID id, ProductRequest productRequest) {
        Set<String> mediaURLs = productRequest.getMediaURLs();
        if (CollectionUtils.isEmpty(mediaURLs))
            return;

        List<ProductMedia> productMedia = createProductMedias(id, mediaURLs);
        productMediaService.addProductMedia(productMedia);
    }

    private List<ProductMedia> createProductMedias(UUID productId, Set<String> mediaUrls) {
        List<ProductMedia> productMedia = new ArrayList<>(mediaUrls.size());
        for (String url : mediaUrls) {
            productMedia.add(new ProductMedia(productId, url));
        }
        return productMedia;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public void deleteProduct(UUID id) {
        Product product = productRepository
                .findById(id).orElseThrow(ProductNotFoundException::new);
        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (UserDetailsUtils.hasRole(Role.ADMIN) || isProductOwner(product, authenticatedUser))
            productRepository.delete(product);
        else
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "delete", "a product");

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('VERIFIED_VENDOR')")
    public void updateProduct(UUID id, ProductRequest productRequest) {
        ProductValidation.validateProduct(productRequest);
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);

        User authenticatedUser = UserDetailsUtils.getAuthenticatedUser();
        if (!UserDetailsUtils.hasRole(Role.ADMIN) && !isProductOwner(product, authenticatedUser))
            throw new UnauthorizedAccessException(authenticatedUser.getId().toString(), "update", "a product");

        product = modelMapper.map(productRequest, Product.class);
        productRepository.save(product);
    }

    private boolean isProductOwner(Product product, User authenticatedUser) {
        return product.getVendorId().equals(authenticatedUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return modelMapper.map(product, ProductResponse.class);
    }
}
