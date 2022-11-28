package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, byte[]> {
    @Override
    <S extends Vendor> S save(S vendor);

    @Override
    void deleteById(byte[] id);
}
