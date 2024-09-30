package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationParams extends PaginationParams {
    private long userId;
}
