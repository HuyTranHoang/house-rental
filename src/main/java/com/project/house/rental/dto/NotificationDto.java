package com.project.house.rental.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDto {
    long id;

    long userId;

    String username;

    long propertyId;

    String propertyTitle;

    long commentId;

    boolean isSeen;

    String createdAt;
}
