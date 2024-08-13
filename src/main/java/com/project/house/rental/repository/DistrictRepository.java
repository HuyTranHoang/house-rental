package com.project.house.rental.repository;

import com.project.house.rental.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {

    @Query("SELECT d FROM District d WHERE d.id = :id AND d.isDeleted = false")
    District findByIdWithFilter(long id);

    District findByNameAndCityId(String name, long city_id);
}