package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.dto.params.NotificationParams;
import com.project.house.rental.entity.Notification;
import com.project.house.rental.entity.Notification_;
import com.project.house.rental.mapper.NotificationMapper;
import com.project.house.rental.repository.NotificationRespository;
import com.project.house.rental.service.NotificationService;
import com.project.house.rental.specification.NotificationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getNotificationsByUserId(NotificationParams notificationParams) {

        Specification<Notification> spec = NotificationSpecification.filterByUserId(notificationParams.getUserId());

        if (notificationParams.getPageNumber() < 0) {
            notificationParams.setPageNumber(0);
        }

        if (notificationParams.getPageSize() <= 0) {
            notificationParams.setPageSize(5);
        }

        Pageable pageable = PageRequest.of(
                notificationParams.getPageNumber(),
                notificationParams.getPageSize(),
                Sort.by(Sort.Order.desc(Notification_.CREATED_AT))
        );

        Page<Notification> notificationPage = notificationRespository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(notificationPage);

        List<NotificationDto> notificationDtoList = notificationPage.stream()
                .map(notificationMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", notificationDtoList
        );
    }


    @Override
    public void updateSeenStatus(long notificationId) {
        Notification notification = notificationRespository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        notification.setSeen(true);
        notificationRespository.save(notification);
    }

    @Override
    public void markAllAsRead(long userId) {
        List<Notification> notifications = notificationRespository.findAllByUserIdAndSeenFalse(userId);
        notifications.forEach(notification -> notification.setSeen(true));
        notificationRespository.saveAll(notifications);
    }
}
