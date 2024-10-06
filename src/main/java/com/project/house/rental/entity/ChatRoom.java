package com.project.house.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoom {

    @Id
    String id;

    @Column(name = "sender_id")
    long senderId;

    @Column(name = "receiver_id")
    long receiverId;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;
}
