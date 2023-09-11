package com.example.preordering.repository;

import com.example.preordering.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByClientUsernameAndSender(String clientUsername, String sender);

    List<JoinRequest> findByCompanyUsernameAndSender(String companyUsername, String sender);
}
