package com.project.house.rental.dto;

import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteDto {

    long userId;

    String username;

    long propertyId;

    String propertyTitle;
}
