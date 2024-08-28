package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteParams extends PaginationParams {
    long userId;

    long propertyId;

    String propertyTitle;

    double propertyPrice;

    private String sortBy = "createdAtDesc";

}
