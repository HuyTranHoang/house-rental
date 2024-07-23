package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmenityParams extends PaginationParams {
    private String name;
    private String sortBy;
}
