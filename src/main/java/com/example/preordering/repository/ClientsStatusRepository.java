package com.example.preordering.repository;

import com.example.preordering.entity.ClientsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsStatusRepository extends JpaRepository<ClientsStatus, Long> {



    @Query(
            "SELECT cl.status FROM ClientsStatus cl WHERE cl.client.username = ?1"
    )
    Long getReports(String username);

    @Query(
            "SELECT cs.status FROM ClientsStatus cs WHERE cs.client.username = ?1"
    )
    int getStatus(String username);
}
