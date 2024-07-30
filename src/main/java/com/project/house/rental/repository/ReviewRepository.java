package com.project.house.rental.repository;

import com.project.house.rental.entity.Review;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends GenericRepository<Review>, JpaSpecificationExecutor<Review> {
}
