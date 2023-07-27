package com.example.preordering.repository;

import com.example.preordering.entity.UserAdminSettingsOfTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdminSettingsOfTimetableRepository extends JpaRepository<UserAdminSettingsOfTimetable, Long> {

}
