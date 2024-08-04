package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleParams extends PaginationParams {
    private String name;

    private String authorities;

    private String sortBy = "createdAtDesc";
}
