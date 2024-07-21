package com.project.house.rental.specification;

import com.project.house.rental.entity.City_;
import com.project.house.rental.entity.District;
import com.project.house.rental.entity.District_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DistrictSpecification {
    public static Specification<District> searchByName(String name) {
        return (root, query, cb) -> {
            if(!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(District_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<District> filterByCities(String cityName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(cityName)) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] citiesName = cityName.split(",");
            for (String city : citiesName) {
                city = city.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(District_.CITY).get(City_.NAME)), "%" + city + "%"));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
