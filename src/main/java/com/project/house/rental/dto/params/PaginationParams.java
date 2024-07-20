package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class PaginationParams {
    @Value("${app.pagination.default-page-number}")
    private int pageNumber;

    @Value("${app.pagination.default-page-size}")
    private int pageSize;
}
