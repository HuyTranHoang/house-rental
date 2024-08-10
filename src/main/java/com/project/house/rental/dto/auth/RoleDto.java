package com.project.house.rental.dto.auth;

import jakarta.validation.constraints.NotEmpty;
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
    String name;

    List<String> authorityPrivileges;

    Date createdAt;
}
