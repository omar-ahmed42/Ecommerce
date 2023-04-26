package com.omarahmed42.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Override
    <S extends Customer> S save(S customer);

    @Override
    void deleteById(UUID id);

    @Override
    boolean existsById(UUID id);

    @Override
    Optional<Customer> findById(UUID id);
}
