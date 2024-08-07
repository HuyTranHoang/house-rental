package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên loại phòng")
    @Size(max = 100, message = "Tên loại phòng không được vượt quá 100 kí tự")
    String name;

    Date createdAt;
}
