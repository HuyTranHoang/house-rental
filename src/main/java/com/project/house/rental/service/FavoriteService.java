package com.project.house.rental.service;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface FavoriteService {

    List<FavoriteDto> getAllFavorites();

    FavoriteDto getFavoriteById(FavoritePrimaryKey favoritePrimaryKey);

    List<FavoriteDto> getFavoriteByUserId(long userId);

    List<FavoriteDto> getFavoriteByPropertyId(long propertyId);

    FavoriteDto createFavorite(FavoriteDto favoriteDto, HttpServletRequest request);

    void deleteFavoriteById(FavoritePrimaryKey favoritePrimaryKey);

    FavoriteDto toDto(Favorite favorite);

    Favorite toEntity(FavoriteDto favoriteDto);

    Map<String, Object> getAllFavoritesWithParams(FavoriteParams favoriteParams);

}
