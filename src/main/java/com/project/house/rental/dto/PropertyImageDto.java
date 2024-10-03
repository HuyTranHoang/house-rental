package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyImageDto {

    long id;

    @NotEmpty(message = "Url không được để trống")
    String imageUrl;

    @NotEmpty(message = "PublicId không được để trống")
    String publicId;

    long propertyId;

}
