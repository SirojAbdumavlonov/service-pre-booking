package com.example.preordering.repository;

import com.example.preordering.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(
            "SELECT c FROM Category c"
    )
    List<Category> findAll();

    Category findByTitle(String categoryName);

    @Transactional
    @Modifying
    void deleteByCategoryId(Long categoryId);

    Category findByCategoryId(Long categoryId);

    boolean existsByCategoryId(Long categoryId);


}
