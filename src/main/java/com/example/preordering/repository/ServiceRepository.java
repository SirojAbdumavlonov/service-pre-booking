package com.example.preordering.repository;

import com.example.preordering.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByServiceIdInAndStatus(List<Long> ids, String status);

    @Query(
            "SELECT s.serviceId, s FROM Service s WHERE s.status = 'ACTIVE'"
    )
    HashMap<Long, Service> findAllServices();

    Optional<Service> findByServiceIdAndCompany_CompanyIdAndStatus(Long serviceId, Long companyId, String status);

    @Query(
            "SELECT s FROM Service s WHERE s.company.category.categoryId = ?1 AND s.status = 'ACTIVE'"
    )
    List<Service> getAllServicesByCategoryId(Long categoryId);
    @Query(
            "SELECT s.occupationName FROM Service s WHERE s.company.companyId = ?1" +
                    " AND ?2 MEMBER OF s.usernamesOfEmployees AND s.status = 'ACTIVE'"
    )
    List<String> occupationNames(Long companyId,
                                 String usernames);
    boolean existsByOccupationNameAndCompany_CompanyIdAndStatus(
            String title, Long companyId, String status);

    @Query(
            "SELECT s.serviceId FROM Service s WHERE s.company.companyId = ?1 AND s.status = 'ACTIVE'"
    )
    List<Long> findByCompanyId(Long companyId);

    @Query(
            "SELECT s.occupationName FROM Service s WHERE ?1 MEMBER OF s.usernamesOfEmployees" +
                    " AND s.status = 'ACTIVE'"
    )
    List<String> getServicesNamesOfThisUserAdminAndCategory(String usernameOfEmployee);

//    boolean existsByOccupationNameAndUsernamesOfEmployees(
//            String occupationName, List<String> employeeUsername);
    @Query(
            "UPDATE Service s SET s.status = 'DELETED' WHERE s.serviceId IN (?1)"
    )
    void deleteServices(List<Long> servicesId);
}
