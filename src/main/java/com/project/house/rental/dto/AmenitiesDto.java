package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenitiesDto {
    long id;

    @NotEmpty(message = "Amenities name is required")
    String name;
}
