package com.project.house.rental.dto;

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

    String userName;

    long propertyId;

    String propertyTitle;
}
