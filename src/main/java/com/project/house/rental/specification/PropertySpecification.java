package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {
    
    public static Specification<Property> searchByCriteria(Property propertyParams) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (StringUtils.hasLength(propertyParams.getDistrict().getName())) {
                Join<Property, District> districtJoin = root.join(Property_.DISTRICT);
                predicate = cb.and(predicate, cb.like(cb.lower(districtJoin.get(District_.NAME)), "%" + propertyParams.getDistrict().getName().toLowerCase() + "%"));
            }

            if (StringUtils.hasLength(propertyParams.getCity().getName())) {
                Join<Property, City> cityJoin = root.join(Property_.CITY);
                predicate = cb.and(predicate, cb.like(cb.lower(cityJoin.get(City_.NAME)), "%" + propertyParams.getCity().getName().toLowerCase() + "%"));
            }

            if (propertyParams.getPrice() != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Property_.PRICE), propertyParams.getPrice()));
            }

            if (StringUtils.hasLength(propertyParams.getRoomType().getName())) {
                Join<Property, RoomType> roomTypeJoin = root.join(Property_.ROOM_TYPE);
                predicate = cb.and(predicate, cb.like(cb.lower(roomTypeJoin.get(RoomType_.NAME)), "%" + propertyParams.getRoomType().getName().toLowerCase() + "%"));
            }

            if (propertyParams.getArea() != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Property_.AREA), propertyParams.getArea()));
            }

            return predicate;
        };
    }

    public static Specification<Property> filterByCriteria(String filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasLength(filter)) {
                String filterLower = "%" + filter.toLowerCase() + "%";

                Join<Property, District> districtJoin = root.join(Property_.DISTRICT);
                predicates.add(cb.like(cb.lower(districtJoin.get(District_.NAME)), filterLower));

                
                Join<Property, City> cityJoin = root.join(Property_.CITY);
                predicates.add(cb.like(cb.lower(cityJoin.get(City_.NAME)), filterLower));

               
                Join<Property, RoomType> roomTypeJoin = root.join(Property_.ROOM_TYPE);
                predicates.add(cb.like(cb.lower(roomTypeJoin.get(RoomType_.NAME)), filterLower));

               
                Join<Property, Amenity> amenityJoin = root.join("amenities");
                predicates.add(cb.like(cb.lower(amenityJoin.get("name")), filterLower));

                
                predicates.add(cb.like(cb.lower(root.get(Property_.PRICE).as(String.class)), filterLower));

                
                predicates.add(cb.like(cb.lower(root.get(Property_.AREA).as(String.class)), filterLower));
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}