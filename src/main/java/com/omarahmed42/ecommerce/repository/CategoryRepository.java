package com.omarahmed42.ecommerce.repository;

import com.omarahmed42.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Override
    <S extends Category> S save(S entity);

    @Override
    <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities);

    @Override
    void deleteById(Integer integer);
    void deleteCategoryByName(String name);

    @Override
    boolean existsById(Integer integer);
    boolean existsByName(String name);

    Optional<Category> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Category SET name = :name WHERE id = :id", nativeQuery = true)
    void updateName(@Param("id") int id, @Param("name") String name);
}
