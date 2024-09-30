package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "User id is required")
    long userId;

    String username;

    @NotEmpty(message = "Property id is required")
    long propertyId;

    String propertyTitle;

    @NotEmpty(message = "Comment id is required")
    long commentId;

    boolean isSeen;

    String createdAt;
}
