package com.example.preordering.repository;

import com.example.preordering.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Long> findMastersByCompanyId(Long companyId);

    @Query(
            "SELECT c.companyName FROM Company c WHERE c.directorUsername =:username"
    )
    String getCompanyName(@Param("username") String username);

    @Query(
            "SELECT c FROM Company c WHERE c.companyName =: companyName " +
                    "AND c.directorUsername =: username"
    )
    Company getByCompanyNameAndCompanyDirectorUsername(@Param("companyName") String companyName,
                                                       @Param("username") String username);


}
