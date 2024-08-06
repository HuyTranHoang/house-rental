package com.project.house.rental.service;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

public interface FavoriteService extends GenericService<Favorite, FavoriteDto>{
    Map<String, Object> getAllFavoritesWithParams(FavoriteParams favoriteParams);

    FavoriteDto create(FavoriteDto favoriteDto, HttpServletRequest request) throws CustomRuntimeException;

}
