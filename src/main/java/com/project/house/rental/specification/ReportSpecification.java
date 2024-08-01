package com.project.house.rental.specification;

import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.Report_;
import com.project.house.rental.entity.auth.UserEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {
    public static Specification<Report> filterByUsername(String username) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(username)) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] usernames = username.split(",");
            for (String user : usernames) {
                user = user.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(Report_.USER).get(UserEntity_.USERNAME)), "%" + user + "%"));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
