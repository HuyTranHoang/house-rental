package com.project.house.rental.specification;

import com.project.house.rental.entity.Comment;
import com.project.house.rental.entity.Comment_;
import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CommoentSpecification {

    public static Specification<Comment> filterByPropertyId(long propertyId) {
        return (root, query, cb) -> {
            if (propertyId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Comment_.PROPERTY).get(Property_.ID), propertyId);
        };
    }

    public static Specification<Comment> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Comment_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Comment> searchByUsernamePropertyTitle(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search))
                return cb.conjunction();

            return cb.or(
                    cb.like(root.get(Comment_.USER).get(UserEntity_.USERNAME), "%" + search + "%"),
                    cb.like(root.get(Comment_.PROPERTY).get(Property_.TITLE), "%" + search + "%")
            );
        };
    }
}
