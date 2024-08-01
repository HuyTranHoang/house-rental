package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;



public class PropertySpecification {

    public static Specification<Property> searchByCityDistrictLocation(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search)) {
                return cb.conjunction();
            }

            Join<Property, City> cityJoin = root.join(Property_.city);
            Join<Property, District> districtJoin = root.join(Property_.district);

            return cb.or(
                    cb.like(cb.lower(cityJoin.get(City_.name)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(districtJoin.get(District_.name)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(Property_.location)), "%" + search.toLowerCase() + "%")
            );

        };
    }

    public static Specification<Property> filterByCityId(long cityId) {
        return (root, query, cb) -> {
            if (cityId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.city).get(City_.id), cityId);
        };
    }

    public static Specification<Property> filterByDistrictId(long districtId) {
        return (root, query, cb) -> {
            if (districtId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.district).get(District_.id), districtId);
        };
    }

    public static Specification<Property> filterByRoomTypeId(long roomTypeId) {
        return (root, query, cb) -> {
            if (roomTypeId == 0) {
                return cb.conjunction();
            }

            return cb.equal(root.get(Property_.roomType).get(RoomType_.id), roomTypeId);
        };
    }

    public static Specification<Property> filterByPrice(double minPrice, double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == 0 && maxPrice == 0) {
                return cb.conjunction();
            }

            if (minPrice == 0) {
                return cb.lessThanOrEqualTo(root.get(Property_.price), maxPrice);
            }

            if (maxPrice == 0) {
                return cb.greaterThanOrEqualTo(root.get(Property_.price), minPrice);
            }

            return cb.between(root.get(Property_.price), minPrice, maxPrice);
        };
    }

    public static Specification<Property> filterByArea(double minArea, double maxArea) {
        return (root, query, cb) -> {
            if (minArea == 0 && maxArea == 0) {
                return cb.conjunction();
            }

            if (minArea == 0) {
                return cb.lessThanOrEqualTo(root.get(Property_.area), maxArea);
            }

            if (maxArea == 0) {
                return cb.greaterThanOrEqualTo(root.get(Property_.area), minArea);
            }

            return cb.between(root.get(Property_.area), minArea, maxArea);
        };
    }

}