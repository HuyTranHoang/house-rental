package com.project.house.rental.specification;

import com.project.house.rental.entity.Transaction;
import com.project.house.rental.entity.Transaction_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserEntity_;
import jakarta.persistence.criteria.Join;
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

    public static Specification<Transaction> searchByTransactionIdUsername(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search)) {
                return cb.conjunction();
            }

            Join<Transaction, UserEntity> userJoin = root.join(Transaction_.USER);

            return cb.or(
                    cb.like(cb.lower(root.get(Transaction_.TRANSACTION_ID)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(userJoin.get(UserEntity_.USERNAME)), "%" + search.toLowerCase() + "%")
            );

        };
    }


    public static Specification<Transaction> filterByStatus(String status) {
        return (root, query, cb) -> {
            if(!StringUtils.hasLength(status))
                return cb.conjunction();

            return cb.equal(root.get(Transaction_.STATUS), status);
        };
    }

    public static Specification<Transaction> filterByTransactionType(String transactionType) {
        return (root, query, cb) -> {
            if(!StringUtils.hasLength(transactionType))
                return cb.conjunction();

            return cb.equal(root.get(Transaction_.TYPE), transactionType);
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
