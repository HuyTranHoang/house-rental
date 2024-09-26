package com.project.house.rental.specification;

import com.project.house.rental.entity.CommentReport;
import com.project.house.rental.entity.CommentReport_;
import com.project.house.rental.entity.auth.UserEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentReportSpecification {
    public static Specification<CommentReport> filterByUsername(String username) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(username)) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] usernames = username.split(",");
            for (String user : usernames) {
                user = user.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(CommentReport_.USER).get(UserEntity_.USERNAME)), "%" + user + "%"));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<CommentReport> filterByStatus(String status) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(status)) {
                return cb.conjunction();
            }

            return cb.equal(root.get(CommentReport_.STATUS), status);
        };
    }

    public static Specification<CommentReport> filterByCategory(String categoryList) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(categoryList)) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] categories = categoryList.split(",");
            for (String category : categories) {
                category = category.trim().toUpperCase();
                predicates.add(cb.equal(root.get(CommentReport_.CATEGORY), category));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
