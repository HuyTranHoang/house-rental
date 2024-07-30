package com.project.house.rental.entity.auth;

import com.project.house.rental.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority extends BaseEntity {
    String privilege;
}
