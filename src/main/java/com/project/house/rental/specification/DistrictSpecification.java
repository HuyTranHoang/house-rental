package com.project.house.rental.specification;

import com.project.house.rental.entity.City_;
import com.project.house.rental.entity.District;
import com.project.house.rental.entity.District_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class DistrictSpecification {
    public static Specification<District> searchByName(String name) {
        return (root, query, cb) -> {
            if(!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(District_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<District> filterByCity(long cityId) {
        return (root, query, cb) -> {
            if(cityId == 0)
                return cb.conjunction();

            return cb.equal(root.get(District_.CITY).get(City_.ID), cityId);
        };
    }
}
