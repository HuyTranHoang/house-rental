package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Property> searchByArea(Double area) {
        return (root, query, cb) -> {
            if (area != null) {
                return cb.equal(root.get(Property_.AREA), area);
            }
            return cb.conjunction();
        };
    }
    public static Specification<Property> searchByRoomTypeName(String roomTypeName) {
        return (root, query, cb) -> {
            if (StringUtils.hasLength(roomTypeName)) {
                Join<Property, RoomType> roomTypeJoin = root.join(Property_.ROOM_TYPE);
                return cb.like(cb.lower(roomTypeJoin.get(RoomType_.NAME)), "%" + roomTypeName.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }
    public static Specification<Property> searchByPrice(Double price) {
        return (root, query, cb) -> {
            if (price != null) {
                return cb.equal(root.get(Property_.PRICE), price);
            }
            return cb.conjunction();
        };
    }
    public static Specification<Property> searchByCityName(String cityName) {
        return (root, query, cb) -> {
            if (StringUtils.hasLength(cityName)) {
                Join<Property, City> cityJoin = root.join(Property_.CITY);
                return cb.like(cb.lower(cityJoin.get(City_.NAME)), "%" + cityName.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }
    public static Specification<Property> searchByDistrictName(String districtName) {
        return (root, query, cb) -> {
            if (StringUtils.hasLength(districtName)) {
                Join<Property, District> districtJoin = root.join(Property_.DISTRICT);
                return cb.like(cb.lower(districtJoin.get(District_.NAME)), "%" + districtName.toLowerCase() + "%");
            }
            return cb.conjunction();
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