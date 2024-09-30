package com.project.house.rental.service.impl;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.entity.Notification;
import com.project.house.rental.mapper.NotificationMapper;
import com.project.house.rental.repository.NotificationRespository;
import com.project.house.rental.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRespository notificationRespository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRespository notificationRespository, NotificationMapper notificationMapper) {
        this.notificationRespository = notificationRespository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        Notification notification = notificationMapper.toEntity(notificationDto);

        notification.setSeen(false);

        notification = notificationRespository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public List<NotificationDto> getNotificationsByUserId(long userId) {
        return notificationRespository.getNotificationsByUserId(userId)
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public void updateSeenStatus(long notificationId) {
        Notification notification = notificationRespository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        notification.setSeen(true);
        notificationRespository.save(notification);
    }
}
