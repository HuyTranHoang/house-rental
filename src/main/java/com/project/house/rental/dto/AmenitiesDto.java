package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenitiesDto {
    long id;

    @NotEmpty(message = "Amenities name is required")
    @Size(max = 100, message = "The amenities cannot exceed 100 characters")
    String name;
}
