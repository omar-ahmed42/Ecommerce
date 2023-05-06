package com.omarahmed42.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Category;
import com.omarahmed42.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Category> findCategoriesById(UUID productId);
}