package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistrictDto {
    long id;

    @NotEmpty(message = "District name is required")
    String name;

    long cityId;

    String cityName;
}