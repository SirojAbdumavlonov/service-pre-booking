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

    @Query(
            "SELECT us.order.services.occupationName FROM UserAdminTimetable us WHERE us.date = ?1 " +
                    "AND us.userAdmin.username = ?2"
    )
    List<String> getServiceNames(LocalDate localDate, String username);


    UserAdminTimetable getByOrder_OrderId(Long orderId);

    @Query(
            "SELECT u FROM UserAdminTimetable u WHERE (u.date = ?1 AND u.start >= ?2) OR (u.date = ?3 AND ?4 >= u.start)"
    )
    List<UserAdminTimetable> getOrdersWithin12Hours(LocalDate date, LocalTime start, LocalDate after12Hours, LocalTime startPlus12Hs);
}
