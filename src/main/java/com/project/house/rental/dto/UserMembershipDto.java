package com.project.house.rental.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMembershipDto {
    private long id;
    private long userId;
    private String username;
    private Long membershipId;
    private String membershipName;
    private Date startDate;
    private Date endDate;
    private Integer totalPriorityLimit;
    private Integer totalRefreshLimit;
    private Integer priorityPostsUsed;
    private Integer refreshesPostsUsed;
    private String status;
}
