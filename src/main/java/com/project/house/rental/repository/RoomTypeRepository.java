package com.project.house.rental.repository;

import com.project.house.rental.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long>, JpaSpecificationExecutor<RoomType> {
    RoomType findByNameIgnoreCase(String name);

    @Query("SELECT r FROM RoomType r WHERE r.id = :id AND r.isDeleted = false")
    RoomType findByIdWithFilter(long id);
}
