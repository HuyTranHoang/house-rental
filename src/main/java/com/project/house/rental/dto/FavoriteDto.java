package com.project.house.rental.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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

    Date createdAt;
}
