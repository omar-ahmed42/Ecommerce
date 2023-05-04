package com.omarahmed42.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}