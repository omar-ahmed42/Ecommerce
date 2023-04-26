package com.omarahmed42.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.CustomerNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.exception.WishlistNotFoundException;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.repository.WishlistRepository;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void addWishlist(Wishlist wishlist) {
        if (customerRepository.findById(wishlist.getCustomerId()).isEmpty()){
            throw new CustomerNotFoundException("Customer not found");
        } else if (productRepository.findById(wishlist.getProductId()).isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }

        wishlistRepository.save(wishlist);
    }

    @Transactional
    @Override
    public void deleteWishlist(WishlistPK wishlistPK) {
        wishlistRepository.findById(wishlistPK)
                .ifPresentOrElse(wishlistRepository::delete,
                        () -> {throw new WishlistNotFoundException("Wishlist not found");});
    }

    @Transactional
    @Override
    public void updateWishlist(Wishlist wishlist) {
        wishlistRepository
                .findById(new WishlistPK(wishlist.getCustomerId(), wishlist.getProductId()))
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));

        wishlistRepository.save(wishlist);
    }

    @Transactional
    @Override
    public Wishlist getWishlist(WishlistPK wishlistPK) {
        return wishlistRepository
                .findById(wishlistPK)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));
    }
}
