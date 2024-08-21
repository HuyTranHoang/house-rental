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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoritePrimaryKey that = (FavoritePrimaryKey) o;
        return userId == that.userId && propertyId == that.propertyId;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(userId);
        result = 31 * result + Long.hashCode(propertyId);
        return result;
    }
}
