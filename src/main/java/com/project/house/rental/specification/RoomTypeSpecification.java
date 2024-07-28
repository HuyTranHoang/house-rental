package com.project.house.rental.specification;

import com.project.house.rental.entity.RoomType;
import com.project.house.rental.entity.RoomType_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class RoomTypeSpecification {
    public static Specification<RoomType> searchByName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(RoomType_.NAME)), "%" + name.toLowerCase() + "%");

        };
    }
}
