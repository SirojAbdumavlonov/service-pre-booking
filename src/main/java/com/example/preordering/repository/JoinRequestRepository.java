package com.example.preordering.repository;

import com.example.preordering.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByEmployeeIdAndSenderOrderByCreatedDate(Long employeeId, String sender);

    List<JoinRequest> findByCompanyIdAndSender(Long companyId, String sender);

    @Query(
            "UPDATE JoinRequest j SET j.requestStatus = ?1 WHERE j.companyId = ?2 AND j.employeeId = ?3"
    )
    void updateRequestTo(String status, Long companyId, Long employeeId);
}
