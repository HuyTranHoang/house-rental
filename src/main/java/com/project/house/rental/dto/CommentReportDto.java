package com.project.house.rental.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentReportDto {
    long id;

    long userId;
    String username;

    long commentId;
    String comment;

    @NotEmpty(message = "Vui lòng nhập lí do báo cáo")
    String reason;

    @NotEmpty(message = "Vui lòng chọn danh mục báo cáo")
    String category;

    String status;

    Date createdAt;
}
