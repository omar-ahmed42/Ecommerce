package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Cartitems;
import com.omarahmed42.ecommerce.model.CartitemsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<Cartitems, CartitemsPK> {
}
