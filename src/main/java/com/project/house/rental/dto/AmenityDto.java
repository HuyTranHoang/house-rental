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
public class AmenityDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên tiện ích")
    @Size(max = 100, message = "Tên tiện ích không được vượt quá 100 kí tự")
    String name;
}
