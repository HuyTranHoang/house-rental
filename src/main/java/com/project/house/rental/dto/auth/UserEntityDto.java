package com.project.house.rental.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntityDto {
    long id;

    @NotEmpty(message = "Vui lòng nhập tên đăng nhập")
    @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 kí tự")
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    String password;

    @NotEmpty(message = "Vui lòng nhập email")
    @Size(max = 100, message = "Email không được vượt quá 100 kí tự")
    @Email(message = "Vui lòng nhập email hợp lệ")
    String email;

    String phoneNumber;

    String firstName;

    String lastName;

    String avatarUrl;

    boolean isActive;

    boolean isNonLocked;

    List<String> roles;

    List<String> authorities;
}
