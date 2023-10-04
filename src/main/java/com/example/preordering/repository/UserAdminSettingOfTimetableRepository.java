package com.example.preordering.repository;

import com.example.preordering.entity.UserAdminSettingsOfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserAdminSettingOfTimetableRepository extends JpaRepository<UserAdminSettingsOfTimetable, Long> {
    @Query(
            "SELECT u FROM UserAdminSettingsOfTimetable u WHERE u.userAdmin.username = ?1"
    )
    UserAdminSettingsOfTimetable findByusername(String username);
}
