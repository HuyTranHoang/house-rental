package com.project.house.rental.entity;

import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
public class Review extends BaseEntity {

    int rating;

    String comment;

    @ManyToOne
    @JoinColumn(name = "property_id")
    Property property;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

}
