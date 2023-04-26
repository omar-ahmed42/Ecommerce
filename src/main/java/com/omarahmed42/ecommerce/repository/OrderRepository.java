package com.omarahmed42.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {
    @Override
    Optional<Orders> findById(UUID id);

    @Override
    boolean existsById(UUID id);

    @Override
    <S extends Orders> S save(S order);

    @Override
    void deleteById(UUID id);
}
