package com.example.preordering.repository;

import com.example.preordering.entity.UserAdminSettingsOfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdminDefaultTimetableRepository extends JpaRepository<UserAdminSettingsOfTimetable, Long> {

    @Query(
            "SELECT us FROM UserAdminSettingsOfTimetable us WHERE us.userAdmin.username = ?1"
    )
    UserAdminSettingsOfTimetable findByUserAdminUsername(String username);
}
