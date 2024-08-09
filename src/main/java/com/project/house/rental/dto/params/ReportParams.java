package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportParams extends PaginationParams {
    private String username;

    private String title;

    private String status;

    private String category;

    private String sortBy = "createdAtDesc";
}
