package com.project.house.rental.repository;

import com.project.house.rental.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long>, JpaSpecificationExecutor<Amenity> {
    Amenity findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Amenity a WHERE a.id = :id AND a.isDeleted = false")
    Amenity findByIdWithFilter(long id);
}
