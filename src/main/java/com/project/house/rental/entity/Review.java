package com.project.house.rental.entity;

import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE reviews SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int rating;

    String comment;

    @ManyToOne
    @JoinColumn(name = "property_id")
    Property property;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Column(name = "is_deleted")
    boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    Date updatedAt;
}
