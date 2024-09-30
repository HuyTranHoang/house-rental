package com.project.house.rental.controller;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserId(@PathVariable long userId) {
        List<NotificationDto> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto notification = notificationService.createNotification(notificationDto);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Void> updateSeenStatus(@PathVariable long notificationId) {
        notificationService.updateSeenStatus(notificationId);
        return ResponseEntity.noContent().build();
    }
}
