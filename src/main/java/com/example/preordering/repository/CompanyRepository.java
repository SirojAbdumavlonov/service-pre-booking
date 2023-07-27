package com.example.preordering.repository;

import com.example.preordering.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(
            "SELECT c FROM Company c WHERE c.category.categoryId = ?1"
    )
    List<Company> findByCategoryId(Long categoryId);
    @Query(
          value = "SELECT c FROM Company c WHERE c.category.categoryId = ?1 AND c.companyId = ?2"
    )
    Optional<Company> findByCategoryIdAndCompanyId(Long categoryId, Long companyId);

    @Query(
            "SELECT c.mastersId FROM Company c WHERE c.companyId = ?1"
    )
    Set<Long> findMastersByCompanyId(Long companyId);



}
