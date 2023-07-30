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
    List<String> findUsernameOfUserAdmins(@Param("ids") List<Long> ids);

    @Query(
            "SELECT us.username, us FROM UserAdmin us"
    )
    HashMap<String, UserAdmin> allUserAdmins();


}
