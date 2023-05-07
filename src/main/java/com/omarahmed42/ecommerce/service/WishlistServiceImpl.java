package com.omarahmed42.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.PageResponse;
import com.omarahmed42.ecommerce.DTO.ProductResponse;
import com.omarahmed42.ecommerce.enums.ProductSort;
import com.omarahmed42.ecommerce.exception.WishlistAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.WishlistNotFoundException;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.WishlistRepository;
import com.omarahmed42.ecommerce.util.PageUtils;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, CustomerRepository customerRepository) {
        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
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

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') || (hasRole('CUSTOMER') && (principal.user.id == #customerId))")
    public PageResponse<ProductResponse> getWishlistItems(UUID customerId, Integer page, Integer size,
            ProductSort sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, size, PageUtils.getSortOrder(sortOrder, "product"));
        Page<Product> products = wishlistRepository
                .findAllProductsByCustomer(customerRepository.getReferenceById(customerId), pageable);
        List<ProductResponse> productResponse = modelMapper.map(products.getContent(),
                new TypeToken<List<ProductResponse>>() {
                }.getType());
        return new PageResponse<>(products, productResponse);
    }
}
