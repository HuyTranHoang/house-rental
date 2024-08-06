package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Date;

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

    public static Specification<Favorite> filterByPropertyTitle(String propertyTitle) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(propertyTitle))
                return cb.conjunction();

            return cb.like(root.get(Favorite_.PROPERTY).get(Property_.TITLE), propertyTitle.toLowerCase());
        };
    }

    public static Specification<Favorite> filterByPropertyCreated(Date propertyCreated) {
        return (root, query, cb) -> {
            if (propertyCreated == null)
                return cb.conjunction();

            return cb.equal(root.get(Favorite_.PROPERTY).get(Property_.CREATED_AT), propertyCreated);
        };
    }
}
