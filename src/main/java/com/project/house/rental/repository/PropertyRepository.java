package com.project.house.rental.repository;

import com.project.house.rental.entity.Property;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends GenericRepository<Property> , JpaSpecificationExecutor<Property> {

}
