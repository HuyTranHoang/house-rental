package com.project.house.rental.dto.auth;

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
public class AuthorityDto {

    long id;

    @NotEmpty(message = "Quyền hạn không được để trống")
    String privilege;

    Date createdAt;
}
