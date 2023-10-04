package com.example.preordering.repository;

import com.example.preordering.entity.Company;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(
            "SELECT c FROM Company c WHERE c.directorUsername = ?1 AND c.status = 'ACTIVE'"
    )
    Company findByDirectorUsername(String username);

    @Query(
            "SELECT c FROM Company c WHERE c.category.categoryId = ?1 AND c.functionality = ?2 AND c.status = 'ACTIVE'"
    )
    List<Company> findByCategoryIdAndFunctionality(Long categoryId, String functionality);

    @Query(
            "SELECT c FROM Company c WHERE c.category.categoryId = ?1 AND c.status = 'ACTIVE'"
    )
    List<Company> findByCategoryId(Long categoryId);
    @Query(
          value = "SELECT c FROM Company c WHERE c.category.title = ?1 AND c.directorUsername= ?2 " +
                  "AND c.status = 'ACTIVE'"
    )
    Optional<Company> findByCategoryNameAndDirectorUsername(String categoryName, String directorUsername);

    @Query(
            value = "SELECT c FROM Company c WHERE c.category.categoryId = ?1 AND c.companyId= ?2" +
                    " AND c.status = 'ACTIVE'"
    )
    Optional<Company> findByCategoryIdAndCompanyId(Long categoryId, Long companyId);
    @Query(
            "SELECT c.mastersId FROM Company c WHERE c.companyId = ?1"
    )
    List<Long> findMastersByCompanyId(Long companyId);

    @Query(
            "SELECT c.companyName FROM Company c WHERE c.directorUsername = ?1 AND c.status = 'ACTIVE'"
    )
    String getCompanyName(String username);

    @Query(
            "SELECT c FROM Company c WHERE c.companyName = ?1 " +
                    "AND c.directorUsername = ?2 AND c.status = 'ACTIVE'"
    )
    Company getByCompanyNameAndCompanyDirectorUsername(String companyName,
                                                       String username);

    List<Company> findAllByCategory_CategoryIdAndStatus(Long categoryId,
                                               Pageable pageable, String status);

    @Query(
            "SELECT c FROM Company c WHERE c.directorUsername = ?1 AND c.status = 'ACTIVE'"
    )
    Company getCompanyByDirectorUsername(String directorUsername);

    boolean existsByMastersIdIsContainingAndCompanyIdAndStatus(
            Long mastersId, Long companyId, String status);

    boolean existsByDirectorUsernameAndFunctionalityAndStatus(
            String directorUsername, String functionality, String status);

    @Query(
            "UPDATE Company c SET c.status = 'DELETED' WHERE c.directorUsername = ?1"
    )
    void deleteCompany(String directorUsername);

    @Query(
            "SELECT c FROM Company c WHERE c.companyUsername = ?1"
    )
    Company getByCompanyUsername(String companyUsername);



    boolean existsByCompanyUsername(String companyUsername);

    Company findByCompanyId(Long companyId);

    @Transactional
    @Modifying
    @Query(
            "UPDATE Company c SET c.servicesId = ?1 WHERE c.companyId = ?2"
    )
    void saveServicesId(List<Long> newServicesId, Long companyId);

}
