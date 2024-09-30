package com.project.house.rental.mapper;

import com.project.house.rental.dto.NotificationDto;
import com.project.house.rental.entity.Notification;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(NotificationMapperDecorator.class)
public interface NotificationMapper {

    @Mapping(source = "seen", target = "isSeen")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "propertyTitle")
    @Mapping(source = "comment.id", target = "commentId")
    NotificationDto toDto(Notification notification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toEntity(NotificationDto notificationDto);
}
