package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity_;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;


public class PropertySpecification {

    public static Specification<Property> searchByCityDistrictLocation(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search)) {
                return cb.conjunction();
            }

            Join<Property, City> cityJoin = root.join(Property_.CITY);
            Join<Property, District> districtJoin = root.join(Property_.DISTRICT);

            return cb.or(
                    cb.like(cb.lower(cityJoin.get(City_.NAME)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(districtJoin.get(District_.NAME)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(Property_.LOCATION)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(Property_.TITLE)), "%" + search.toLowerCase() + "%")
            );

        };
    }

    public static Specification<Property> filterByCityId(long cityId) {
        return (root, query, cb) -> {
            if (cityId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.CITY).get(City_.ID), cityId);
        };
    }

    public static Specification<Property> filterByDistrictId(long districtId) {
        return (root, query, cb) -> {
            if (districtId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.DISTRICT).get(District_.ID), districtId);
        };
    }

    public static Specification<Property> filterByRoomTypeId(long roomTypeId) {
        return (root, query, cb) -> {
            if (roomTypeId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.ROOM_TYPE).get(RoomType_.ID), roomTypeId);
        };
    }

    public static Specification<Property> filterByPrice(double minPrice, double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == 0 && maxPrice == 0) {
                return cb.conjunction();
            }

            if (minPrice == 0) {
                return cb.lessThanOrEqualTo(root.get(Property_.PRICE), maxPrice);
            }

            if (maxPrice == 0) {
                return cb.greaterThanOrEqualTo(root.get(Property_.PRICE), minPrice);
            }

            return cb.between(root.get(Property_.PRICE), minPrice, maxPrice);
        };
    }

    public static Specification<Property> filterByArea(double minArea, double maxArea) {
        return (root, query, cb) -> {
            if (minArea == 0 && maxArea == 0) {
                return cb.conjunction();
            }

            if (minArea == 0) {
                return cb.lessThanOrEqualTo(root.get(Property_.AREA), maxArea);
            }

            if (maxArea == 0) {
                return cb.greaterThanOrEqualTo(root.get(Property_.AREA), minArea);
            }

            return cb.between(root.get(Property_.AREA), minArea, maxArea);
        };
    }

    public static Specification<Property> filterByCreatedDate(int numOfDays) {
        return (root, query, cb) -> {
            if (numOfDays == 0) {
                return cb.conjunction();
            }

            // Calculate the correct threshold date
            LocalDate dateThreshold = LocalDate.now().minusDays(numOfDays);
            Date sqlDateThreshold = Date.valueOf(dateThreshold);

            // Convert Property_.CREATED_AT to LocalDate for comparison
            Expression<LocalDate> createdDate = cb.function("DATE", LocalDate.class, root.get(Property_.CREATED_AT));

            return cb.greaterThanOrEqualTo(createdDate, sqlDateThreshold.toLocalDate());
        };
    }


    public static Specification<Property> filterByStatus(String status) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(status)) {
                return cb.conjunction();
            }
            return cb.equal(root.get(Property_.status), status);
        };
    }

    public static Specification<Property> filterByBlocked(String status) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(status)) {
                return cb.conjunction();
            }

            if (status.equals("true")) {
                return cb.equal(root.get(Property_.IS_BLOCKED), true);
            } else {
                return cb.equal(root.get(Property_.IS_BLOCKED), false);
            }
        };
    }

    public static Specification<Property> filterByHidden(String status) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(status)) {
                return cb.conjunction();
            }

            if (status.equals("true")) {
                return cb.equal(root.get(Property_.IS_HIDDEN), true);
            } else {
                return cb.equal(root.get(Property_.IS_HIDDEN), false);
            }
        };
    }

    public static Specification<Property> filterByUserId(long userId) {
        return (root, query, cb) -> {
            if (userId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.USER).get(UserEntity_.ID), userId);
        };
    }

    public static Specification<Property> filterByPriority(boolean priority) {
        return (root, query, cb) -> cb.equal(root.get(Property_.IS_PRIORITY), priority);
    }

}