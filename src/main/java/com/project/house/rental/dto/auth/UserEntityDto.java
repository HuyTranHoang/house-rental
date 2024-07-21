package com.project.house.rental.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

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
