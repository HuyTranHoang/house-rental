package com.project.house.rental.repository;

import com.project.house.rental.entity.City;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends GenericRepository<City>, JpaSpecificationExecutor<City> {
    City findByNameIgnoreCase(String name);
}
