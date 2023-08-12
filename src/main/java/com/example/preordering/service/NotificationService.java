package com.example.preordering.service;

import com.example.preordering.constants.UserRoles;
import com.example.preordering.entity.Notification;
import com.example.preordering.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getNotifications(String userRole){
        return switch (userRole){
            case "CLIENT" -> notificationRepository.getAllNotificationsByRole(UserRoles.CLIENT);
            case "EMPLOYER" -> notificationRepository.getAllNotificationsByRole(UserRoles.EMPLOYER);
            case "EMPLOYEE" -> notificationRepository.getAllNotificationsByRole(UserRoles.EMPLOYEE);
            default -> throw new IllegalStateException("Unexpected value: " + userRole);
        };
    }
}
