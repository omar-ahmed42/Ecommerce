package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;

public interface CartService {
    void addCartItem(Cartitems cartitems);
    void deleteCartItem(Cartitems cartitems);
    void updateCartItem(CartitemsPK cartitemsPK, Cartitems newCartItem);
}
