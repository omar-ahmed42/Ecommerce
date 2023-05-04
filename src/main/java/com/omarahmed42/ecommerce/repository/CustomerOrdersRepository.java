package com.omarahmed42.ecommerce.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.CustomerOrdersPK;
import com.omarahmed42.ecommerce.model.OrderDetails;

@Repository
public interface CustomerOrdersRepository extends JpaRepository<CustomerOrders, CustomerOrdersPK> {
    Slice<OrderDetails> findOrdersByCustomerId(Byte id, Pageable pageable);
}
