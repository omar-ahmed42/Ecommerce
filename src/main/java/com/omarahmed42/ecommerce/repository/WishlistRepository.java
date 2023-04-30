package com.omarahmed42.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistPK> {
    @Override
    <S extends Wishlist> S save(S entity);

    @Override
    void deleteById(WishlistPK wishlistPK);

    @Query("SELECT productByProductId FROM Wishlist WHERE customerId = :#{#wishlistPK.customerId} AND productId = :#{#wishlistPK.productId}")
    Optional<Product> findProductByWishlistPK(@Param("wishlistPK") WishlistPK wishlistPK);
}
