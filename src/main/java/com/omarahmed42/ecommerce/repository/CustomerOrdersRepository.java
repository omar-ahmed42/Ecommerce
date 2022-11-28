package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.CustomerOrdersPK;
import com.omarahmed42.ecommerce.model.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrdersRepository extends JpaRepository<CustomerOrders, CustomerOrdersPK> {
    @Override
    <S extends CustomerOrders> S save(S entity);

    @Override
    void deleteById(CustomerOrdersPK customerOrdersPK);

    @Override
    boolean existsById(CustomerOrdersPK customerOrdersPK);

    @Override
    Optional<CustomerOrders> findById(CustomerOrdersPK customerOrdersPK);

    Slice<Orders> findOrdersByCustomerId(Byte id, Pageable pageable);
}
