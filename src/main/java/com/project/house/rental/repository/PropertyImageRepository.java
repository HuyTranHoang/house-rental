package com.project.house.rental.repository;

import com.project.house.rental.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageRepository extends GenericRepository<PropertyImage>, JpaSpecificationExecutor<PropertyImage> {
    PropertyImage findByImageUrl(String imageUrl);
    List<PropertyImage> findAllByPropertyId(long propertyId);
}
