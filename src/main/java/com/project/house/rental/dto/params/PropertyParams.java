package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyParams extends PaginationParams {

    private String search;

    private long cityId;

    private long districtId;

    private long roomTypeId;

    private double minPrice;

    private double maxPrice;

    private double minArea;

    private double maxArea;

    private int numOfDays;

    private String status;

    private String sortBy = "createdAtDesc";

    private long userId;

    private String isBlocked;
}
