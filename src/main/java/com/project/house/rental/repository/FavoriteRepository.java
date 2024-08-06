package com.project.house.rental.repository;

import com.project.house.rental.entity.Favorite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends GenericRepository<Favorite>, JpaSpecificationExecutor<Favorite> {

}
