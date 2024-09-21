package com.project.house.rental.specification;

import com.project.house.rental.entity.Membership_;
import com.project.house.rental.entity.UserMembership;
import com.project.house.rental.entity.UserMembership_;
import org.springframework.data.jpa.domain.Specification;

public class UserMembershipSpecifition {
    public static Specification<UserMembership> filterByMembership(long membershipId) {
        return (root, query, cb) -> {
            if(membershipId == 0)
                return cb.conjunction();

            return cb.equal(root.get(UserMembership_.MEMBERSHIP).get(Membership_.ID), membershipId);
        };
    }
}
