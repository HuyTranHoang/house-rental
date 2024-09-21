package com.project.house.rental.entity;

import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_memberships")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE user_memberships SET is_deleted = true WHERE id = ?")
//@FilterDef(name = FilterConstant.DELETE_USER_MEMBERSHIP_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
//@Filter(name = FilterConstant.DELETE_USER_MEMBERSHIP_FILTER, condition = FilterConstant.CONDITION)
public class UserMembership extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "membership_id", nullable = false)
    Membership membership;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate;

    @Column(name = "total_priority_limit")
    Integer totalPriorityLimit;

    @Column(name = "total_refresh_limit")
    Integer totalRefreshLimit;

    @Column(name = "priority_posts_used", columnDefinition = "integer default 0")
    Integer priorityPostsUsed = 0;

    @Column(name = "refreshes_posts_used", columnDefinition = "integer default 0")
    Integer refreshesPostsUsed = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('ACTIVE', 'EXPIRED') default 'EXPIRED'")
    Status status = Status.EXPIRED;

    public enum Status {
        ACTIVE,
        EXPIRED
    }
}
