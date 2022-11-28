package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, byte[]> {
    @Override
    <S extends Customer> S save(S customer);

    @Override
    void deleteById(byte[] id);

    @Override
    boolean existsById(byte[] id);

    @Override
    Optional<Customer> findById(byte[] id);
}
