package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PropertyParams extends  PaginationParams{
    private String districtName;
    private String cityName;
    private Double price;
    private String roomTypeName;
    private Double area;
    private String filter;
    private String sortBy = "createdAtDesc";
}
