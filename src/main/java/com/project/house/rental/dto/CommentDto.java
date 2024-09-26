package com.project.house.rental.dto;


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
public class CommentDto {

    long id;

    @Size(min = 16, max = 500, message = "Nhận xét phải từ 16 đến 500 ký tự")
    String comment;

    long propertyId;

    String propertyTitle;

    long userId;

    String userName;

    String userAvatar;

    Date createdAt;
}
