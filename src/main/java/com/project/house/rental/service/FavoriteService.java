package com.project.house.rental.service;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;

import java.util.List;

public interface FavoriteService {

    List<FavoriteDto> getAllFavorites();

    FavoriteDto getFavoriteById(FavoritePrimaryKey favoritePrimaryKey);

    List<FavoriteDto> getFavoriteByUserId(long userId);

    List<FavoriteDto> getFavoriteByPropertyId(long propertyId);

    FavoriteDto createFavorite(FavoriteDto favoriteDto);

    void deleteFavoriteById(FavoritePrimaryKey favoritePrimaryKey);

    FavoriteDto toDto(Favorite favorite);

    Favorite toEntity(FavoriteDto favoriteDto);
}
