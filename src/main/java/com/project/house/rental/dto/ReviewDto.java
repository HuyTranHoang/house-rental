package com.project.house.rental.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {

    long id;

    int rating;

    String comment;

    long propertyId;

    String propertyTitle;

    long userId;

    String userName;
}
