package com.project.house.rental.repository;

import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoritePrimaryKey> {

    @Query("SELECT f FROM Favorite f WHERE f.favoritePrimaryKey = :favoritePrimaryKey")
    Favorite findByCompositeKey(FavoritePrimaryKey favoritePrimaryKey);

    @Query("SELECT f FROM Favorite f WHERE f.favoritePrimaryKey = :favoritePrimaryKey AND f.isDeleted = false")
    Favorite findByCompositeKeyWithFilter(FavoritePrimaryKey favoritePrimaryKey);

    @Query("SELECT f FROM Favorite f WHERE f.favoritePrimaryKey.userId = :userId AND f.isDeleted = false")
    List<Favorite> findByUserIdWithFilter(long userId);

    @Query("SELECT f FROM Favorite f WHERE f.favoritePrimaryKey.propertyId = :propertId AND f.isDeleted = false")
    List<Favorite> findByPropertyIdWithFilter(long propertId);
}
