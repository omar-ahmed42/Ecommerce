package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.CartItem;
import com.omarahmed42.ecommerce.model.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, CartItemPK> {
}
