package com.project.house.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @OneToMany(mappedBy = "city")
    List<District> districts;
}
