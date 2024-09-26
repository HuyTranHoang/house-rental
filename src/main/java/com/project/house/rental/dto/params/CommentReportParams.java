package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReportParams extends PaginationParams {
    private String username;

    private String comment;

    private String status;

    private String category;

    private String sortBy = "createdAtDesc";
}
