package com.project.house.rental.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProfileDto {

    @NotEmpty(message = "Vui lòng nhập tên")
    String firstName;

    @NotEmpty(message = "Vui lòng nhập họ")
    String lastName;

    @Pattern(regexp = "(\\+84|0)\\d{9,10}", message = "Số điện thoại không hợp lệ")
    String phoneNumber;
}
