package com.omarahmed42.ecommerce.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.omarahmed42.ecommerce.DTO.ProductRequest;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.service.ProductService;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ProductRepository productRepository;

    private User user;
    private Vendor vendor;

    private User prepareUser() {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("not.a.real.email@test.imagination");
        user.setPassword("user_pass");
        user.setPhoneNumber("0123456789");
        user.setActive(false);
        user.setBanned(false);
        return user;
    }

    private ProductResponse prepareProductResponse(UUID id) {
        ProductResponse expected = new ProductResponse();
        expected.setId(id);
        expected.setName("Modern phone");
        expected.setDescription("This is a description");
        expected.setPrice(BigDecimal.ONE.setScale(2));
        expected.setStock(5);
        expected.setRating(0D);
        return expected;
    }

    private Product prepareProduct() {
        Product product = new Product();
        product.setName("Modern phone");
        product.setDescription("This is a description");
        product.setPrice(BigDecimal.ONE.setScale(2));
        product.setStock(5);
        product.setRating(0D);
        return product;
    }

    @BeforeEach
    public void init() {
        user = prepareUser();

        user = userRepository.saveAndFlush(user);

        vendor = new Vendor();
        vendor.setId(user.getId());
        vendor.setVerifiedVendor(true);
        vendor = vendorRepository.saveAndFlush(vendor);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        vendorRepository.delete(vendor);
        userRepository.delete(user);
        user = null;
        vendor = null;
    }

    private ProductRequest prepareProductRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Modern phone");
        productRequest.setDescription("This is a description");
        productRequest.setPrice(BigDecimal.ONE.setScale(2));
        productRequest.setStock(5);
        return productRequest;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addProduct_EmptyName_ThrowsMissingFieldException() {
        ProductRequest productRequest = prepareProductRequest();
        productRequest.setName(null);
        UUID vendorId = vendor.getId();
        Assertions.assertThrows(MissingFieldException.class,
                () -> productService.addProduct(vendorId, productRequest),
                "Product name is missing");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addProduct_SuccessfulllyReturnsProductResponse() {
        ProductRequest productRequest = prepareProductRequest();
        ProductResponse actual = productService.addProduct(vendor.getId(), productRequest);
        ProductResponse expected = prepareProductResponse(actual.getId());

        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt", "categoryProductsById", "productItemsById",
                        "vendorByVendorId", "productMediaById", "productReviewsById", "wishlistsById", "cartitemsById",
                        "rating")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteProduct_ThrowsProductNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(nonExistentId),
                "Product not found");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getProduct_SuccessfullyReturnsProductResponse() {
        Product product = prepareProduct();
        product.setVendorId(vendor.getId());
        product = productRepository.save(product);

        ProductResponse expected = prepareProductResponse(product.getId());
        ProductResponse actual = productService.getProduct(product.getId());
        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(expected);
    }
    
    @Test
    public void getProduct_ThrowsProductNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProduct(nonExistentId),
                "Product not found");
    }

}
