package com.project.house.rental.specification;

import com.project.house.rental.entity.Notification;
import com.project.house.rental.entity.Notification_;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

    public static Specification<Notification> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Notification_.USER).get(UserEntity_.ID), userId);
        };
    }
}
