package com.omarahmed42.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    @Override
    <S extends Vendor> S save(S vendor);

    @Override
    void deleteById(UUID id);
}
