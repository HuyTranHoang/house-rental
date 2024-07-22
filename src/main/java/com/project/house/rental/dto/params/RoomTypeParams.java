package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeParams extends PaginationParams {
    private String name;
    private String sortBy;
}
