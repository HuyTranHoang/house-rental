package com.project.house.rental.service.impl;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.entity.Notification;
import com.project.house.rental.mapper.NotificationMapper;
import com.project.house.rental.repository.NotificationRespository;
import com.project.house.rental.service.NotificationService;
import org.springframework.stereotype.Service;

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
        notification = notificationRespository.save(notification);
        return notificationMapper.toDto(notification);
    }
}
