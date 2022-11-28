package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistPK> {
    @Override
    <S extends Wishlist> S save(S entity);

    @Override
    void deleteById(WishlistPK wishlistPK);
}
