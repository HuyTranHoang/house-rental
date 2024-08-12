package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserParams extends PaginationParams {
    String search; // username, email, phone number

    String roles;

    Boolean isNonLocked;

    private String sortBy = "createdAtDesc";
}
