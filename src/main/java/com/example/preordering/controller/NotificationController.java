package com.example.preordering.controller;

import com.example.preordering.entity.UserAdmin;
import com.example.preordering.model.NotificationRequest;
import com.example.preordering.model.NotificationResponse;
import com.example.preordering.service.FCMService;
import com.example.preordering.service.NotificationService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final UserAdminService userAdminService;
    private final NotificationService notificationService;
    private final FCMService fcmService;

    @GetMapping("/{username}/notifications")
    public ResponseEntity<?> getNotifications(@PathVariable String username){

        UserAdmin userAdmin =
                userAdminService.getByUsername(username);

        return ResponseEntity.ok(
                notificationService.getNotifications(userAdmin.getRole())
        );
    }
    @PostMapping("/notification")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest request) throws ExecutionException, InterruptedException {
        fcmService.sendMessageToToken(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
}
