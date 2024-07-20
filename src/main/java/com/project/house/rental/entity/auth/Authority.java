package com.project.house.rental.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String privilege;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;
}
