package com.example.preordering.controller;

import com.example.preordering.constants.UserRoles;
import com.example.preordering.entity.Client;
import com.example.preordering.entity.UserAdmin;
import com.example.preordering.service.NotificationService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final UserAdminService userAdminService;
    private final NotificationService notificationService;

    @GetMapping("/{username}/notifications")
    public ResponseEntity<?> getNotifications(@PathVariable String username){

        if(userAdminService.getByUsername(username) instanceof Client){
            return ResponseEntity.ok(
                    notificationService.getNotifications("CLIENT"));
        }
        UserAdmin userAdmin = (UserAdmin) userAdminService.getByUsername(username);

        return ResponseEntity.ok(
                notificationService.getNotifications(userAdmin.getRole())
        );
    }
}
