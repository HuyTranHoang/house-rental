package com.project.house.rental.entity.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoritePrimaryKey {

    long userId;

    long propertyId;
}
