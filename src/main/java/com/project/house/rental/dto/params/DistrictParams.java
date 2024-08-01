package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictParams extends PaginationParams {
    private String name;

    private long cityId;

    private String sortBy = "createdAtDesc";
}
