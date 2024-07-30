package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewParams extends PaginationParams {
    int rating;

    long userId;

    long propertyId;

    String sortBy;
}
