package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipParams extends PaginationParams {
    private String name;
    private String sortBy = "createdAtDesc";
}
