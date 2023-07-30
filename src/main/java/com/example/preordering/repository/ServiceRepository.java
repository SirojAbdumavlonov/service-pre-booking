package com.example.preordering.repository;

import com.example.preordering.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByServiceIdIn(List<Long> ids);

    Optional<Service> findByServiceIdAndCompany_CompanyId(Long serviceId, Long companyId);

    @Query(
            "SELECT s.occupationName FROM Service s WHERE s.company.companyId =: companyId" +
                    " AND s.usernames in (:usernames)"
    )
    List<String> occupationNames(@Param("companyId") Long companyId,
                                 @Param("usernames") String usernames);
}
