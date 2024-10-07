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
public class AdvertisementDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tiêu đề quảng cảo")
    @Size(max = 100, message = "Tiêu đề không được vượt quá 100 kí tự")
    String name;

    String description;

    String imageUrl;

    Date createdAt;
}
