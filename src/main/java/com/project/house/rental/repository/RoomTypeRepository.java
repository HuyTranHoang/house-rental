package com.project.house.rental.repository;

import com.project.house.rental.entity.RoomType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends GenericRepository<RoomType>, JpaSpecificationExecutor<RoomType> {
    RoomType findByNameIgnoreCase(String name);
}
