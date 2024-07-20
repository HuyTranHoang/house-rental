package com.project.house.rental.specification;

import com.project.house.rental.entity.City;
import com.project.house.rental.entity.City_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CitySpecification {
    public static Specification<City> searchByName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(City_.NAME)), "%" + name.toLowerCase() + "%");

        };
    }
}
