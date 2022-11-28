package com.omarahmed42.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Override
    <S extends VerificationToken> S save(S entity);

    @Override
    void delete(VerificationToken entity);

    @Override
    void deleteById(Long aLong);

    void deleteByToken(String token);

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findById(Long id);
}