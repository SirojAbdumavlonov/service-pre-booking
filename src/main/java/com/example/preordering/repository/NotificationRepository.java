package com.example.preordering.repository;

import com.example.preordering.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(
            "SELECT n FROM Notification n WHERE n.userRole = ?1 OR n.userRole = 0"
    )
    List<Notification> getAllNotificationsByRole(int role);

}
