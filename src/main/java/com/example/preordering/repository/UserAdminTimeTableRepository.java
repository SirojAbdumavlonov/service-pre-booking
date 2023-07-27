package com.example.preordering.repository;

import com.example.preordering.entity.UserAdminTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface UserAdminTimeTableRepository extends JpaRepository<UserAdminTimetable, Long> {
    @Query(
            "SELECT us.start FROM UserAdminTimetable us " +
                    "WHERE us.date = ?1 AND us.userAdmin.username = ?2"
    )
    List<LocalTime> getStartsByDate(LocalDate date, String username);
    @Query(
            "SELECT us.finish FROM UserAdminTimetable us " +
                    "WHERE us.date = ?1 AND us.userAdmin.username = ?2"
    )
    List<LocalTime> getEndsByDate(LocalDate date, String username);

}
