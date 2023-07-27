package com.example.preordering.repository;

import com.example.preordering.entity.UserAdmin;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {
    Optional<UserAdmin> findByEmailOrUsername(String email, String username);
    UserAdmin findByUsername(String username);

    @Query(
            "SELECT u.userAdminId FROM UserAdmin u WHERE u.username = ?1"
    )
    Long findUserAdminIdByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(
            "SELECT us.username FROM UserAdmin us WHERE us.userAdminId in (:ids)"
    )
    Set<String> findUsernameOfUserAdmins(@Param("ids") Set<Long> ids);


}
