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
@Table(name = "properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE properties SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "title")
    String title;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "price")
    Double price;

    @Column(name = "location")
    String location;

    @Column(name = "area")
    Double area;

    @Column(name = "num_rooms")
    Integer numRooms;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING', 'RESOLVED') DEFAULT 'PENDING'")
    PropertyStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id")
    RoomType roomType;

    @Column(name = "is_deleted")
    boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    Date updatedAt;

    public enum PropertyStatus {
        PENDING,
        RESOLVED
    }
}
