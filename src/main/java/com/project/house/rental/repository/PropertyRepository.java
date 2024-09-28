package com.project.house.rental.repository;

import com.project.house.rental.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    @Query("SELECT p FROM Property p WHERE p.id = :id AND p.isDeleted = false")
    Property findByIdWithFilter(long id);

    @Query("SELECT p FROM Property p WHERE p.isDeleted = false AND p.isBlocked = false AND p.isPriority = true AND p.priorityExpiration < :priorityExpiration")
    List<Property> findByPriorityExpirationBeforeAndPriorityTrue(Date priorityExpiration);

    List<Property> findByRefreshedAtBefore(Date refreshDay);

}
