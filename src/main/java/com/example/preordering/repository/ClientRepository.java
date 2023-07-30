package com.example.preordering.repository;

import com.example.preordering.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmailOrUsername(String email, String username);

    Client findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(
            value = "SELECT c.password FROM Client c WHERE c.email = ?1"
    )
    String passwordOfEmail(String email);

    @Query(
            "SELECT c.username, c FROM Client c"
    )
    HashMap<String, Client> allClients();
}
