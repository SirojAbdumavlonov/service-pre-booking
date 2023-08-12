package com.example.preordering.repository;

import com.example.preordering.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByServiceIdIn(List<Long> ids);

    @Query(
            "SELECT s.serviceId, s FROM Service s"
    )
    HashMap<Long, Service> findAllServices();

    Optional<Service> findByServiceIdAndCompany_CompanyId(Long serviceId, Long companyId);

    @Query(
            "SELECT s FROM Service s WHERE s.company.category.categoryId = ?1"
    )
    List<Service> getAllServicesByCategoryId(Long categoryId);
    @Query(
            "SELECT s.occupationName FROM Service s WHERE s.company.companyId = ?1" +
                    " AND s.usernames in (?2)"
    )
    List<String> occupationNames(Long companyId,
                                 String usernames);
}
