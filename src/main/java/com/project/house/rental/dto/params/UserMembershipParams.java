package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserMembershipParams extends PaginationParams {
    private long userId;

    private long membershipId;

    private Date startDate;

    private Date endDate;

    private int totalPriorityLimit;

    private int totalRefreshLimit;

    private int priorityPostsUsed;

    private int refreshPostsUsed;
}
