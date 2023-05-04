package com.omarahmed42.ecommerce.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.exception.CartItemNotFoundException;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.MoreThanStockCapacityException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.CartItem;
import com.omarahmed42.ecommerce.model.CartItemPK;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.CartItemsRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemsRepository cartItemRepository;
    private final ProductRepository productRepository;
    private ModelMapper modelMapper;

    public CartServiceImpl(CartItemsRepository cartItemsRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemsRepository;
        this.productRepository = productRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void addCartItem(CartItemDTO cartItemDTO) {
        validateCartItem(cartItemDTO);
        saveCartItem(cartItemDTO);
    }

    private void saveCartItem(CartItemDTO cartItemDTO) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        if (product.getStock() < cartItemDTO.getQuantity())
            throw new MoreThanStockCapacityException("Requested product exceeds the available stock products");

        CartItem cartItem = new CartItem(UserDetailsUtils.getAuthenticatedUser().getId(), cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(product.getPrice());
        cartItem.setSubtotal(calculateSubtotal(cartItemDTO, product.getPrice()));
        cartItemRepository.save(cartItem);
    }

    private BigDecimal calculateSubtotal(CartItemDTO cartItemDTO, BigDecimal unitPrice) {
        if (cartItemDTO == null || cartItemDTO.getQuantity() == null || cartItemDTO.getQuantity() <= 0
                || unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0)
            return BigDecimal.ZERO;

        return unitPrice.multiply(BigDecimal.valueOf(cartItemDTO.getQuantity()));
    }

    private void validateCartItem(CartItemDTO cartItemDTO) {
        if (cartItemDTO == null)
            throw new MissingFieldException("Cart item is missing");
        if (cartItemDTO.getProductId() == null)
            throw new MissingFieldException("Product id is missing");

        if (cartItemDTO.getQuantity() == null)
            throw new MissingFieldException("Quantity is missing");

        if (cartItemDTO.getPrice() == null)
            throw new MissingFieldException("Price is missing");

        if (cartItemDTO.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidInputException("Price cannot be less than 0");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteCartItem(UUID productId) {
        cartItemRepository.delete(cartItemRepository
                .findById(new CartItemPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId))
                .orElseThrow(CartItemNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') || (hasRole('CUSTOMER') && #userId == principal.user.id)")
    public CartItemDTO getCartItem(UUID userId, UUID productId) {
        CartItem cartItem = cartItemRepository.findById(new CartItemPK(userId, productId))
                .orElseThrow(CartItemNotFoundException::new);
        return modelMapper.map(cartItem, CartItemDTO.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public void updateCartItem(UUID productId, CartItemDTO cartItemDTO) {
        validateCartItem(cartItemDTO);
        CartItemPK cartItemPK = new CartItemPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId);
        if (cartItemDTO.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemPK);
            return;
        }

        saveCartItem(cartItemDTO);
    }
}