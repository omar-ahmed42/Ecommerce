package com.omarahmed42.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    <S extends User> boolean exists(Example<S> example);

    boolean existsByEmail(String email);

    @Override
    void deleteById(UUID id);

    @Override
    <S extends User> S save(S entity);

    Optional<User> findByEmail(String email);
}
