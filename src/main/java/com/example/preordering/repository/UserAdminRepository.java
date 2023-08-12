package com.example.preordering.repository;

import com.example.preordering.entity.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {
    @Query(
            "SELECT us FROM UserAdmin us WHERE (us.email = ?1 OR us.username = ?2) AND us.role = ?3"
    )
    Optional<UserAdmin> findByEmailOrUsernameAndRole(String email, String username, String role);
    Optional<UserAdmin> findByEmailOrUsername(String email, String username);
    UserAdmin findByUsername(String username);

    @Query(
            "SELECT u.userAdminId FROM UserAdmin u WHERE u.username = ?1"
    )
    Long findUserAdminIdByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(
            "SELECT us.username FROM UserAdmin us WHERE us.userAdminId in (?1)"
    )
    List<String> findUsernameOfUserAdmins(List<Long> ids);

    @Query(
            "SELECT us.username, us FROM UserAdmin us"
    )
    HashMap<String, UserAdmin> allUserAdmins();

    @Query(
            "SELECT us.role FROM UserAdmin us WHERE us.username = ?1"
    )
    String getRoleByUsername(String username);

}
