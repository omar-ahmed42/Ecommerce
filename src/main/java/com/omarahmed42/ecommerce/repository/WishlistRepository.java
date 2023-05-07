package com.omarahmed42.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.Wishlist;
import com.omarahmed42.ecommerce.model.WishlistPK;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistPK> {
    @Query("SELECT w.product FROM Wishlist w WHERE w.customerId = :#{#wishlistPK.customerId} AND w.productId = :#{#wishlistPK.productId}")
    Optional<Product> findProductByWishlistPK(@Param("wishlistPK") WishlistPK wishlistPK);

    @Query("SELECT w.product FROM Wishlist w WHERE w.customer =  :#{#customer}")
    Page<Product> findAllProductsByCustomer(@Param("customer") Customer customer, Pageable pageable);
}