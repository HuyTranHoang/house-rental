package com.project.house.rental.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatNotification {
    long id;
    long senderId;
    long receiverId;
    String message;
}
