package com.project.house.rental.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

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

    public PageInfo(Page page) {
        this.number = page.getNumber();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        if (page.getSize() < 0 || page.getSize() > this.maxPageSize) {
            this.size = 10;
        } else {
            this.size = page.getSize();
        }
    }
}
