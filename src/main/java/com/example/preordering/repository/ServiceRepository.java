package com.example.preordering.repository;

import com.example.preordering.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Set<Service> findByServiceIdIn(Set<Long> ids);

    Optional<Service> findByServiceIdAndCompany_CompanyId(Long serviceId, Long companyId);
}
