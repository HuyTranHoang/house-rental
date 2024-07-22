package com.project.house.rental.repository;

import com.project.house.rental.entity.District;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends GenericRepository<District>, JpaSpecificationExecutor<District> {
}