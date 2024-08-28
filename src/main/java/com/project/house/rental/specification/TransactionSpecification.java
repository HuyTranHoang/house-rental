package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TransactionSpecification {

    public static Specification<Transaction> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0)
                return cb.conjunction();

            return cb.equal(root.get(Transaction_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Transaction> filterByStatus(String status) {
        return (root, query, cb) -> {
            if(!StringUtils.hasLength(status))
                return cb.conjunction();

            return cb.equal(root.get(Transaction_.STATUS), status);
        };
    }

    public static Specification<Transaction> filterByAmount(double minAmount, double maxAmount) {
        return (root, query, cb) -> {
            if (minAmount == 0 && maxAmount == 0) {
                return cb.conjunction();
            }

            if (minAmount == 0) {
                return cb.lessThanOrEqualTo(root.get(Transaction_.AMOUNT), maxAmount);
            }

            if (maxAmount == 0) {
                return cb.greaterThanOrEqualTo(root.get(Transaction_.AMOUNT), minAmount);
            }

            return cb.between(root.get(Transaction_.AMOUNT), minAmount, maxAmount);
        };
    }

}