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

    List<Property> findByPriorityExpirationBefore(Date priorityDay);

    @Query("SELECT p FROM Property p WHERE p.isDeleted = false AND p.isBlocked = false AND p.isPriority = true")
    List<Property> findByIsPriorityTrue();

    // Tìm kiếm những bất động sản tương tự, từ nhiều điều kiện đến ít ( để đảm bảo đủ số lượng )
    @Query("SELECT p FROM Property p WHERE p.city.id = :cityId AND p.district.id = :districtId AND p.roomType.id = :roomTypeId AND p.id != :propertyId AND p.isDeleted = false AND p.isBlocked = false AND p.status = 'APPROVED'")
    List<Property> findRelatedProperties(long cityId, long districtId, long roomTypeId, long propertyId);

    @Query("SELECT p FROM Property p WHERE p.city.id = :cityId AND p.roomType.id = :roomTypeId AND p.id != :propertyId AND p.isDeleted = false AND p.isBlocked = false AND p.status = 'APPROVED'")
    List<Property> findRelatedPropertiesByCityAndRoomType(long cityId, long roomTypeId, long propertyId);

    @Query("SELECT p FROM Property p WHERE p.city.id = :cityId AND p.id != :propertyId AND p.isDeleted = false AND p.isBlocked = false AND p.status = 'APPROVED'")
    List<Property> findRelatedPropertiesByCity(long cityId, long propertyId);

    @Query("SELECT p FROM Property p WHERE p.id != :propertyId AND p.isDeleted = false AND p.isBlocked = false AND p.status = 'APPROVED'")
    List<Property> findAllPropertiesExcept(long propertyId);

}
