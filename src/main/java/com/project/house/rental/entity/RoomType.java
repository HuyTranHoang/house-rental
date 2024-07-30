package com.project.house.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_types")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE room_types SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class RoomType extends BaseEntity{

    @Column(name = "name", nullable = false)
    String name;

    @OneToMany(mappedBy = "roomType")
    List<Property> properties;
}
