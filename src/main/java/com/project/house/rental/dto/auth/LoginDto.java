package com.project.house.rental.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LoginDto {

    @NotEmpty(message = "Vui lòng nhập tên đăng nhập")
    String username;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    String password;
}
