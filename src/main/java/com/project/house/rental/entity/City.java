package com.project.house.rental.entity;

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
@Table(name = "cities")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE cities SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class City extends BaseEntity {

    String name;

    @OneToMany(mappedBy = "city")
    List<District> districts;

    @OneToMany(mappedBy = "city")
    List<Property> properties;
}
