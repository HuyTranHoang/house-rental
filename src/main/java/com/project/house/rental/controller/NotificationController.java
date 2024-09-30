package com.project.house.rental.controller;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.dto.params.NotificationParams;
import com.project.house.rental.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getNotificationsByUserId(@ModelAttribute NotificationParams notificationParams) {
        Map<String, Object> notificationsByUserId = notificationService.getNotificationsByUserId(notificationParams);
        return ResponseEntity.ok(notificationsByUserId);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto notification = notificationService.createNotification(notificationDto);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/mark-all-as-read/{userId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Void> updateSeenStatus(@PathVariable long notificationId) {
        notificationService.updateSeenStatus(notificationId);
        return ResponseEntity.noContent().build();
    }
}
