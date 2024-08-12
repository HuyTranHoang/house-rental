package com.project.house.rental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ReviewDto {

    long id;

    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải từ 1 đến 5 sao")
    int rating;

    @Size(min = 16, max = 500, message = "Nhận xét phải từ 16 đến 500 ký tự")
    String comment;

    long propertyId;

    String propertyTitle;

    long userId;

    String userName;

    Date createdAt;
}
