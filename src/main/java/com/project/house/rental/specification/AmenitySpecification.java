package com.project.house.rental.specification;
import com.project.house.rental.entity.Amenity;
import com.project.house.rental.entity.Amenity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class AmenitySpecification {
    public static Specification<Amenity> searchByName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(Amenity_.NAME)), "%" + name.toLowerCase() + "%");

        };
    }
}
