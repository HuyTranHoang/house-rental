package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDto {
    long id;

    long userId;
    String username;

    long propertyId;
    String title;

    @NotEmpty(message = "Vui lòng nhập lí do báo cáo")
    String reason;


}
