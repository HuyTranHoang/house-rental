package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE districts SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class DistrictDto {
    long id;

    @NotEmpty(message = "District name is required")
    @Size(max = 100, message = "The district cannot exceed 100 characters")
    String name;

    @NotEmpty(message = "City id is required")
    long cityId;

    String cityName;
}