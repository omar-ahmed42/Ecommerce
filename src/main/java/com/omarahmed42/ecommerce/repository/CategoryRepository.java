package com.omarahmed42.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omarahmed42.ecommerce.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    void deleteCategoryByName(String name);

    boolean existsByName(String name);

    Optional<Category> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Category SET name = :name WHERE id = :id", nativeQuery = true)
    void updateName(@Param("id") int id, @Param("name") String name);
}
