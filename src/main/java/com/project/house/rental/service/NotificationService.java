package com.project.house.rental.service;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.dto.params.NotificationParams;

import java.util.Map;

public interface NotificationService {

    NotificationDto createNotification(NotificationDto notificationDto);

    Map<String, Object> getNotificationsByUserId(NotificationParams notificationParams);

    void updateSeenStatus(long notificationId);

    void markAllAsRead(long userId);
}
