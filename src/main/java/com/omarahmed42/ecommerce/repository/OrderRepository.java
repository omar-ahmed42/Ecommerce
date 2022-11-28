package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, byte[]> {
    @Override
    Optional<Orders> findById(byte[] id);

    @Override
    boolean existsById(byte[] id);

    @Override
    <S extends Orders> S save(S order);

    @Override
    void deleteById(byte[] id);
}
