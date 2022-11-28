package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;

public interface WishlistService {
    void addWishlist(Wishlist wishlist);
    void deleteWishlist(WishlistPK wishlistPK);
    void updateWishlist(Wishlist wishlist);

    Wishlist getWishlist(WishlistPK wishlistPK);
}
