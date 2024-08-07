package com.project.house.rental.repository;

import com.project.house.rental.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
    City findByNameIgnoreCase(String name);

    @Query("SELECT c FROM City c WHERE c.id = :id AND c.isDeleted = false")
    City findByIdWithFilter(long id);
}
