package com.project.house.rental.entity;


import jakarta.persistence.*;
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
@Table(name = "districts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE cities SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class District extends BaseEntity {

    String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    City city;

    @OneToMany(mappedBy = "district")
    List<Property> properties;
}