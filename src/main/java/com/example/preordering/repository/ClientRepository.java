package com.example.preordering.repository;

import com.example.preordering.entity.Client;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

//    @Cacheable(value = "clients", key = "#email + '_' + #username")
    Optional<Client> findByEmailOrUsername(String email, String username);

//    @Cacheable(value = "clients", key = "#username")
    Client findByUsername(String username);

//    @Cacheable(value = "clients",key = "#username")
    Boolean existsByUsername(String username);

//    @Cacheable(value = "clients", key = "#email")
    Boolean existsByEmail(String email);

//    @Query(
//            value = "SELECT c.password FROM Client c WHERE c.email = ?1"
//    )
//    String passwordOfEmail(String email);
//
//    @Query(
//            "SELECT c.username, c FROM Client c"
//    )
//
//    HashMap<String, Client> allClients();
}
