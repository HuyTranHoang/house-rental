package com.project.house.rental.dto.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {

    @NotEmpty(message = "Vui lòng nhập tên")
    String name;

    @NotEmpty(message = "Vui lòng nhập email")
    String email;

    @NotEmpty(message = "Vui lòng nhập nội dung")
    String message;
}
