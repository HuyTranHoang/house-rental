package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên quận")
    @Size(max = 100, message = "Tên quận không được vượt quá 100 kí tự")
    String name;

    long cityId;

    String cityName;
}