package com.project.house.rental.repository;

import com.project.house.rental.entity.Amenity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends GenericRepository<Amenity> , JpaSpecificationExecutor<Amenity> {
    Amenity findByNameIgnoreCase(String name);
}
