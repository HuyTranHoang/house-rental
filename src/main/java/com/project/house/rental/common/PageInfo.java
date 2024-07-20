package com.project.house.rental.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class PageInfo {
    private int number;
    private long totalElements;
    private int totalPages;
    private int size;

    @Value("${app.pagination.max-page-size}")
    @JsonIgnore
    private int maxPageSize;

    public PageInfo(int number, long totalElements, int totalPages, int size) {
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;

        if (size < 0 || size > this.maxPageSize) {
            this.size = 10;
        } else {
            this.size = size;
        }
    }
}
