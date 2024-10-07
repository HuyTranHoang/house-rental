package com.project.house.rental.repository;

import com.project.house.rental.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {
    @Query("SELECT a FROM Advertisement a WHERE a.id = :id AND a.isDeleted = false")
    Advertisement findByIdWithFilter(long id);
}
