package com.project.house.rental.specification;

import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.Review_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Review> searchByUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.trim().isEmpty())
                return cb.conjunction();

            return cb.equal(root.get(Review_.USER).get(UserEntity_.USERNAME), username);
        };
    }
}
