package com.project.house.rental.specification;

import com.project.house.rental.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {
    public static Specification<Property> searchByCriteria(String districtName, String cityName, Double price, String amenity, String roomType, Double area) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (StringUtils.hasLength(districtName)) {
                Join<Property, District> districtJoin = root.join(Property_.DISTRICT);
                predicate = cb.and(predicate, cb.like(cb.lower(districtJoin.get(District_.NAME)), "%" + districtName.toLowerCase() + "%"));
            }

            if (StringUtils.hasLength(cityName)) {
                Join<Property, City> cityJoin = root.join(Property_.CITY);
                predicate = cb.and(predicate, cb.like(cb.lower(cityJoin.get(City_.NAME)), "%" + cityName.toLowerCase() + "%"));
            }

            if (price != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Property_.PRICE), price));
            }

            if (StringUtils.hasLength(roomType)) {
                Join<Property, RoomType> roomTypeJoin = root.join(Property_.ROOM_TYPE);
                predicate = cb.and(predicate, cb.like(cb.lower(roomTypeJoin.get(RoomType_.NAME)), "%" + roomType.toLowerCase() + "%"));
            }

            if (area != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Property_.AREA), area));
            }

            return predicate;
        };
    }


    public static Specification<Property> filterByCriteria(String districtNames, String cityNames, String roomTypeNames, String amenities, Double minPrice, Double maxPrice, Double minArea, Double maxArea) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasLength(districtNames)) {
                Join<Property, District> districtJoin = root.join(Property_.DISTRICT);
                String[] districtArray = districtNames.split(",");
                for (String district : districtArray) {
                    district = district.trim().toLowerCase();
                    predicates.add(cb.like(cb.lower(districtJoin.get(District_.NAME)), "%" + district + "%"));
                }
            }

            if (StringUtils.hasLength(cityNames)) {
                Join<Property, City> cityJoin = root.join(Property_.CITY);
                String[] cityArray = cityNames.split(",");
                for (String city : cityArray) {
                    city = city.trim().toLowerCase();
                    predicates.add(cb.like(cb.lower(cityJoin.get(City_.NAME)), "%" + city + "%"));
                }
            }

            if (StringUtils.hasLength(roomTypeNames)) {
                Join<Property, RoomType> roomTypeJoin = root.join(Property_.ROOM_TYPE);
                String[] roomTypeArray = roomTypeNames.split(",");
                for (String roomType : roomTypeArray) {
                    roomType = roomType.trim().toLowerCase();
                    predicates.add(cb.like(cb.lower(roomTypeJoin.get(RoomType_.NAME)), "%" + roomType + "%"));
                }
            }

            if (StringUtils.hasLength(amenities)) {
                Join<Property, Amenity> amenityJoin = root.join("amenities");
                String[] amenityArray = amenities.split(",");
                for (String amenity : amenityArray) {
                    amenity = amenity.trim().toLowerCase();
                    predicates.add(cb.like(cb.lower(amenityJoin.get("name")), "%" + amenity + "%"));
                }
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(Property_.PRICE), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(Property_.PRICE), maxPrice));
            }

            if (minArea != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(Property_.AREA), minArea));
            }

            if (maxArea != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(Property_.AREA), maxArea));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
