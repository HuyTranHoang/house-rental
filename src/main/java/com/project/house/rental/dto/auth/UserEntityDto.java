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

    @NotEmpty(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password is required")
    String password;

    @NotEmpty(message = "Email is required")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Email(message = "Email must be a valid email address")
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
