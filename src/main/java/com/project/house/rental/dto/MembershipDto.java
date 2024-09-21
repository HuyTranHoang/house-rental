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
public class MembershipDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên gói hạng mức")
    @Size(max = 100, message = "Tên hạng mức không được vượt quá 100 kí tự")
    String name;

    double price;

    Integer durationDays;

    Integer priority;

    Integer refresh;

    String description;

    Date createdAt;

    Date updatedAt;
}
