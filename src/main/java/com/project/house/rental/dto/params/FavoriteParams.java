package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FavoriteParams extends PaginationParams {
    long userId;

    long propertyId;

    String propertyTitle;

    double propertyPrice;

    Date propertyDate;

    private String sortBy = "createdAtDesc";

}
