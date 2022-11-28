package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, byte[]> {
    @Override
    <S extends BillingAddress> S save(S entity);

    @Override
    void deleteById(byte[] id);
}
