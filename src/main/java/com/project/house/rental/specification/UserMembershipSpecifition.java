package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserMembershipSpecifition {
    public static Specification<UserMembership> filterByMembership(long membershipId) {
        return (root, query, cb) -> {
            if(membershipId == 0)
                return cb.conjunction();

            return cb.equal(root.get(UserMembership_.MEMBERSHIP).get(Membership_.ID), membershipId);
        };
    }
}
