package com.project.house.rental.common;

import lombok.Data;

@Data
public class PageInfo {
    private int number;
    private long totalElements;
    private int totalPages;
    private int size;

    public PageInfo(int number, long totalElements, int totalPages, int size) {
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;

        if (size < 0 || size > 20) {
            this.size = 10;
        } else {
            this.size = size;
        }
    }
}
