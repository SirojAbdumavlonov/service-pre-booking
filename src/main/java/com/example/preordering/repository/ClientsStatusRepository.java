package com.example.preordering.repository;

import com.example.preordering.entity.ClientsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsStatusRepository extends JpaRepository<ClientsStatus, Long> {


    Optional<ClientsStatus> findByClient_ClientId(Long clientId);
}
