package com.project.house.rental.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_resets")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Column(name = "expires_at")
    Date expiresAt;

    @Column(name = "is_used")
    boolean isUsed;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    Date updatedAt;
}
