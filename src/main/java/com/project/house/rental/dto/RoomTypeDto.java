package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeDto {
    long id;

    @NotEmpty(message = "Room type is required")
    String name;
}
