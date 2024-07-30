package com.project.house.rental.specification;

import com.project.house.rental.entity.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> filterByRating(int rating) {
        return (root, query, cb) -> {
            if (rating == 0)
                return cb.conjunction();

            return cb.equal(root.get("rating"), rating);
        };
    }

    public static Specification<Review> filterByPropertyId(long propertyId) {
        return (root, query, cb) -> {
            if (propertyId == 0)
                return cb.conjunction();

            return cb.equal(root.get("property").get("id"), propertyId);
        };
    }

    public static Specification<Review> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get("user").get("id"), userId);
        };
    }
}
