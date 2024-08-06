package com.project.house.rental.specification;

import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Favorite_;
import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;

public class FavoriteSpecification {
    public static Specification<Favorite> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Favorite_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Favorite> filterByPropertyId(long propertyId) {
        return (root, query, cb) -> {
            if (propertyId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Favorite_.USER).get(Property_.ID), propertyId);
        };
    }
}
