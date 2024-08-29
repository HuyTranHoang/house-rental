package com.project.house.rental.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoritePropertyDto {

    long userId;

    String username;

    List<PropertyDto> properties;
}
