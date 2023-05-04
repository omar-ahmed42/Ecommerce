package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.exception.WishlistAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.WishlistNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import com.omarahmed42.ecommerce.repository.WishlistRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private ModelMapper modelMapper;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void addWishlist(UUID productId) {
        if (wishlistRepository.existsById(new WishlistPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId)))
            throw new WishlistAlreadyExistsException();

        wishlistRepository.save(new Wishlist(UserDetailsUtils.getAuthenticatedUser().getId(), productId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteWishlist(UUID productId) {
        WishlistPK wishlistPK = new WishlistPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId);
        wishlistRepository.delete(wishlistRepository.findById(wishlistPK).orElseThrow(WishlistNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') || (hasRole('CUSTOMER') && (principal.user.id == #customerId))")
    public ProductResponse getWishlist(UUID customerId, UUID productId) {
        WishlistPK wishlistPK = new WishlistPK(customerId, productId);
        Product product = wishlistRepository
                .findProductByWishlistPK(wishlistPK)
                .orElseThrow(WishlistNotFoundException::new);
        return modelMapper.map(product, ProductResponse.class);
    }
}
