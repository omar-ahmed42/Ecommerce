package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.exception.CartItemNotFoundException;
import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;
import com.omarahmed42.ecommerce.repository.CartItemsRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public CartServiceImpl(CartItemsRepository cartItemsRepository) {
        this.cartItemsRepository = cartItemsRepository;
    }

    @Transactional
    @Override
    public void addCartItem(Cartitems cartitems) {
        cartItemsRepository.save(cartitems);
    }

    @Transactional
    @Override
    public void deleteCartItem(Cartitems cartitems) {
        cartItemsRepository
                .findById(new CartitemsPK(cartitems.getCustomerId(), cartitems.getProductId()))
                .ifPresentOrElse(cartItemsRepository::delete, () -> {
                    throw new CartItemNotFoundException("Cart item not found");
                });
    }

    @Transactional
    @Override
    public void updateCartItem(CartitemsPK cartitemsPK, Cartitems newCartItem) {
        cartItemsRepository
                .findById(cartitemsPK)
                .ifPresentOrElse(present -> {
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(newCartItem, present);
                    cartItemsRepository.save(present);
                }, () -> {
                    throw new CartItemNotFoundException("Cart item not found");
                });
    }
}
