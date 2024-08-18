package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewParams extends PaginationParams {
    private int rating;

    private String search;

    private long userId;

    private long propertyId;

    private String sortBy = "createdAtDesc";
}
