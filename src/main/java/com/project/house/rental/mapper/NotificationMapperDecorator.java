package com.project.house.rental.mapper;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.entity.Notification;
import com.project.house.rental.repository.CommentRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class NotificationMapperDecorator implements NotificationMapper {

    @Autowired
    @Qualifier("delegate")
    private NotificationMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Notification toEntity(NotificationDto notificationDto) {
        Notification notification = delegate.toEntity(notificationDto);

        notification.setUser(userRepository.findById(notificationDto.getUserId()).orElse(null));
        notification.setProperty(propertyRepository.findById(notificationDto.getPropertyId()).orElse(null));
        notification.setComment(commentRepository.findById(notificationDto.getCommentId()).orElse(null));

        return notification;
    }
}
