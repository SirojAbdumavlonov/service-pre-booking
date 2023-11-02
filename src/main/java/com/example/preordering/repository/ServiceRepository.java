package com.example.preordering.repository;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.model.ServiceView;
import org.springframework.data.domain.Pageable;
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
            "SELECT s.occupationName FROM Service s WHERE s.serviceId IN (?1) AND s.status = ?2"
    )
    List<String> findByServiceNamesIdInAndStatus(List<Long> ids, String status);


    @Query(
            "SELECT s.serviceId, s FROM Service s WHERE s.status = 'ACTIVE'"
    )
    HashMap<Long, Service> findAllServices();

    Optional<Service> findByServiceIdAndCompanyIdAndStatus(Long serviceId, Long companyId, String status);

    @Query(
            "SELECT s FROM Service s WHERE s.company.category.categoryId = ?1 AND s.status = 'ACTIVE'"
    )
    List<Service> getAllServicesByCategoryId(Long categoryId);
    @Query(
            "SELECT s.occupationName FROM Service s WHERE s.company.id = ?1" +
                    " AND ?2 MEMBER OF s.employeesId AND s.status = 'ACTIVE'"
    )
    List<String> occupationNames(Long companyId,
                                 Long masterId);
    boolean existsByOccupationNameAndCompanyIdAndStatus(
            String title, Long companyId, String status);

    @Query(
            "SELECT s.serviceId FROM Service s WHERE s.company.id = ?1 AND s.status = 'ACTIVE'"
    )
    List<Long> findByCompanyId(Long companyId);

    @Query(
            "SELECT s.occupationName FROM Service s WHERE ?1 MEMBER OF s.employeesId" +
                    " AND s.status = 'ACTIVE'"
    )
    List<String> getServicesNamesOfThisUserAdminAndCategory(Long employeeId);

//    boolean existsByOccupationNameAndUsernamesOfEmployees(
//            String occupationName, List<String> employeeUsername);
    @Query(
            "UPDATE Service s SET s.status = 'DELETED' WHERE s.serviceId IN (?1)"
    )
    void deleteServices(List<Long> servicesId);
    @Query(
            "SELECT s.serviceId FROM Service s WHERE s.occupationName IN (?1)"
    )
    List<Long> findServices(List<String> serviceNames);


    @Query("SELECT NEW com.example.preordering.model.ServiceView(c.name, c.image, c.username, s.price) FROM Service s JOIN Company c " +
                    "ON s.company.id = c.id " +
                    "WHERE s.occupationName IN (?1) AND c.category.categoryId = ?3 AND c.address LIKE ?4%" +
                    "GROUP BY c.id HAVING COUNT(DISTINCT s.occupationName) = ?2 "
                    )
    List<ServiceView> findCompanyWithService(List<String> servicesNames, int quantityOfParams, Long categoryId, String city,
                                             Pageable pageable, String status);
    @Query("SELECT NEW com.example.preordering.model.ServiceView(c.name, c.image, c.username, s.price) FROM Service s JOIN Company c " +
            "ON s.company.id = c.id " +
            "WHERE (c.category.categoryId = ?3 AND c.address LIKE ?4% ) OR s.occupationName = ?1 " +
            "ORDER BY CASE WHEN ?2 = 'asc' THEN s.price " +
            "ELSE -s.price " +
            "END"
    )
    List<ServiceView> findCompanyWithServicePrice(String servicesName, String orderingPrice, Long categoryId, String city,
                                             Pageable pageable, String status);
    @Query("SELECT NEW com.example.preordering.model.ServiceView(c.name, c.image, c.username, s.price) FROM Service s JOIN Company c " +
            "ON s.company.id = c.id " +
            "WHERE c.category.categoryId = ?2 AND c.address LIKE ?3% AND s.occupationName = ?1 " +
            "AND s.price BETWEEN ?4 AND ?5 "
    )
    List<ServiceView> findCompanyWithServicePriceBetween(String servicesName, Long categoryId, String city, Long minVal, Long maxVal,
                                                  Pageable pageable, String status);

}
