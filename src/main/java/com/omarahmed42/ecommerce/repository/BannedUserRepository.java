package com.omarahmed42.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.BannedUser;

@Repository
public interface BannedUserRepository extends JpaRepository<BannedUser, UUID> {
}