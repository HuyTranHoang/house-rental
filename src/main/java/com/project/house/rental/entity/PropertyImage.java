package com.project.house.rental.entity;

import jakarta.persistence.*;
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
@Table(name = "property_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE property_images SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class PropertyImage extends BaseEntity {

    String imageUrl;

    @Column(name = "public_id")
    String publicId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    Property property;
}
