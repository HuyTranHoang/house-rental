package com.project.house.rental.specification;

import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.Review_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ReviewSpecification {

    public static Specification<Review> filterByRating(int rating) {
        return (root, query, cb) -> {
            if (rating == 0)
                return cb.conjunction();

            return cb.equal(root.get(Review_.RATING), rating);
        };
    }

    public static Specification<Review> filterByPropertyId(long propertyId) {
        return (root, query, cb) -> {
            if (propertyId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Review_.PROPERTY).get(Property_.ID), propertyId);
        };
    }

    public static Specification<Review> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Review_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Review> searchByUsernamePropertyTitle(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search))
                return cb.conjunction();

            return cb.or(
                    cb.like(root.get(Review_.USER).get(UserEntity_.USERNAME), "%" + search + "%"),
                    cb.like(root.get(Review_.PROPERTY).get(Property_.TITLE), "%" + search + "%")
            );
        };
    }
}
