package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memberships")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE memberships SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_MEMBERSHIP_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_MEMBERSHIP_FILTER, condition = FilterConstant.CONDITION)
public class Membership extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "price")
    double price;

    @Column(name = "duration_days", nullable = false)
    Integer durationDays;

    @Column(name = "priority", nullable = false)
    Integer priority;

    @Column(name = "refresh", nullable = false)
    Integer refresh;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @OneToMany(mappedBy = "membership")
    List<UserMembership> userMemberships;

}
