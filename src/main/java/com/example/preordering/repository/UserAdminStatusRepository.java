package com.example.preordering.repository;

import com.example.preordering.entity.UserAdminStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdminStatusRepository extends JpaRepository<UserAdminStatus, Long> {

    @Query(
            "SELECT us.adminStatus FROM UserAdminStatus us WHERE us.userAdmin.username = :username"
    )
    int getUserAdminStatusBy(@Param("username") String username);
}
