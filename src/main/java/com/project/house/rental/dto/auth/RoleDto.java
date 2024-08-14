package com.project.house.rental.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto {

    long id;

    @NotEmpty(message = "Tên của vai trò không được để trống")
    @Size(min = 3, max = 50, message = "Tên của vai trò phải có độ dài từ 3 đến 50 ký tự")
    String name;

    @Size(max = 512, message = "Mô tả của vai trò không được vượt quá 255 ký tự")
    String description;

    List<String> authorityPrivileges;

    Date createdAt;
}
