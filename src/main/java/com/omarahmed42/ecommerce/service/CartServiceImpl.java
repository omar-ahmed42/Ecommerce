package com.omarahmed42.ecommerce.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.exception.CartItemNotFoundException;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.MoreThanStockCapacityException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;
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
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.CUSTOMER.toString())")
    public void addCartItem(CartItemDTO cartItemDTO) {
        validateCartItem(cartItemDTO);
        saveCartItem(cartItemDTO);
    }

    private void saveCartItem(CartItemDTO cartItemDTO) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        if (product.getStock() < cartItemDTO.getQuantity())
            throw new MoreThanStockCapacityException("Requested product exceeds the available stock products");

        Cartitems cartItem = new Cartitems(UserDetailsUtils.getAuthenticatedUser().getId(), cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(calculatePrice(cartItemDTO, product.getPrice()));
        cartItemRepository.save(cartItem);
    }

    private BigDecimal calculatePrice(CartItemDTO cartItemDTO, BigDecimal unitPrice) {
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
    @Secured("hasRole(Role.CUSTOMER.toString())")
    public void deleteCartItem(UUID productId) {
        cartItemRepository
                .findById(new CartitemsPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId))
                .ifPresentOrElse(cartItemRepository::delete, () -> {
                    throw new CartItemNotFoundException("Cart item not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    @Secured("hasRole(Role.ADMIN.toString()) || (hasRole(Role.CUSTOMER.toString()) && #userId == principal.user.id)")
    public CartItemDTO getCartItem(UUID userId, UUID productId) {
        Cartitems cartItem = cartItemRepository.findById(new CartitemsPK(userId, productId))
                .orElseThrow(CartItemNotFoundException::new);
        return modelMapper.map(cartItem, CartItemDTO.class);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.CUSTOMER.toString())")
    public void updateCartItem(UUID productId, CartItemDTO cartItemDTO) {
        validateCartItem(cartItemDTO);
        CartitemsPK cartItemPK = new CartitemsPK(UserDetailsUtils.getAuthenticatedUser().getId(), productId);
        if (cartItemDTO.getQuantity() == 0)
            cartItemRepository.deleteById(cartItemPK);

        saveCartItem(cartItemDTO);
    }
}