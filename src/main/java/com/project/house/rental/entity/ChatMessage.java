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
@Table(name = "chat_message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String chatId;

    @Column(name = "sender_id")
    long senderId;

    @Column(name = "receiver_id")
    long receiverId;

    String message;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;
}
