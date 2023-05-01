package com.omarahmed42.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}