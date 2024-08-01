package com.project.house.rental.repository;

import com.project.house.rental.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long>, JpaSpecificationExecutor<PropertyImage> {
    PropertyImage findByImageUrl(String imageUrl);

    List<PropertyImage> findAllByPropertyId(long propertyId);
}
