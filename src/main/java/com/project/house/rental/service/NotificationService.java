package com.project.house.rental.service;

import com.project.house.rental.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    NotificationDto createNotification(NotificationDto notificationDto);

    List<NotificationDto> getNotificationsByUserId(long userId);
}
