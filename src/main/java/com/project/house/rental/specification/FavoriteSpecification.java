package com.project.house.rental.specification;

import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Favorite_;
import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class FavoriteSpecification {
    public static Specification<Favorite> filterByPropertyId(long propertyId) {
        return (root, query, cb) -> {
            if (propertyId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Favorite_.PROPERTY).get(Property_.ID), propertyId);
        };
    }

    public static Specification<Favorite> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Favorite_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Favorite> searchByPropertyTitle(String propertyTitle) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(propertyTitle))
                return cb.conjunction();

            return cb.like(root.get(Favorite_.PROPERTY).get(Property_.TITLE), "%" + propertyTitle.toLowerCase() + "%");
        };
    }
}
