package com.project.house.rental.specification;

import com.project.house.rental.entity.Membership;
import com.project.house.rental.entity.Membership_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MembershipSpecifition {
    public static Specification<Membership> searchByName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(Membership_.NAME)), "%" + name.toLowerCase() + "%");

        };
    }
}
