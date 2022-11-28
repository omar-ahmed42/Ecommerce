package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedUserRepository extends JpaRepository<BannedUser, byte[]> {
    @Override
    <S extends BannedUser> S save(S entity);

    @Override
    void deleteById(byte[] id);
}
