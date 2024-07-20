package com.project.house.rental.dto.params;

import lombok.*;

@Getter
@Setter
public class CityParams extends PaginationParams {
    private String name;
    private String sortBy;
}
