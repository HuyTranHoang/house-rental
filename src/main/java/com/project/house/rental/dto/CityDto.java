package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên thành phố")
    @Size(max = 100, message = "Tên thành phố không được vượt quá 100 kí tự")
    String name;

    Date createdAt;
}
