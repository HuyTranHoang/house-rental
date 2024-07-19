package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto {
    long id;

    @NotEmpty(message = "City name is required")
    String name;
}
