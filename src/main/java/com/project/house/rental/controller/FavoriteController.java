package com.project.house.rental.controller;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.FavoritePropertyDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import com.project.house.rental.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllFavoritesWithParams(@ModelAttribute FavoriteParams favoriteParams) {
        Map<String, Object> favoritesWithParams = favoriteService.getAllFavoritesWithParams(favoriteParams);
        return ResponseEntity.ok(favoritesWithParams);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FavoriteDto>> getAllFavorites() {
        List<FavoriteDto> favoriteDtos = favoriteService.getAllFavorites();
        return ResponseEntity.ok(favoriteDtos);
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<List<FavoriteDto>> getFavoriteByUserId(@PathVariable long userId) {
        List<FavoriteDto> favoriteDtos = favoriteService.getFavoriteByUserId(userId);
        return ResponseEntity.ok(favoriteDtos);
    }

    @GetMapping("/{userId}/userProperty")
    public ResponseEntity<FavoritePropertyDto> getFavoritePropertyByUserId(@PathVariable long userId) {
        FavoritePropertyDto favoritePropertyDto = favoriteService.getFavoritePropertyByUserId(userId);
        return ResponseEntity.ok(favoritePropertyDto);
    }

    @GetMapping("/{propertyId}/property")
    public ResponseEntity<List<FavoriteDto>> getFavoriteByPropertyId(@PathVariable long propertyId) {
        List<FavoriteDto> favoriteDtos = favoriteService.getFavoriteByPropertyId(propertyId);
        return ResponseEntity.ok(favoriteDtos);
    }

    @GetMapping("/{userId}/{propertyId}")
    public ResponseEntity<FavoriteDto> getFavoriteById(@PathVariable long userId, @PathVariable long propertyId) {
        FavoritePrimaryKey favoritePrimaryKey = new FavoritePrimaryKey(userId, propertyId);
        FavoriteDto favoriteDto = favoriteService.getFavoriteById(favoritePrimaryKey);
        return ResponseEntity.ok(favoriteDto);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<FavoriteDto> createFavorite(@RequestBody FavoriteDto favoriteDto, HttpServletRequest request) {
        FavoriteDto createdFavorite = favoriteService.createFavorite(favoriteDto, request);
        return ResponseEntity.ok(createdFavorite);
    }

    @DeleteMapping("/{userId}/{propertyId}")
    public ResponseEntity<FavoriteDto> deleteFavorite(@PathVariable long userId, @PathVariable long propertyId) {
        FavoritePrimaryKey favoritePrimaryKey = new FavoritePrimaryKey(userId, propertyId);
        favoriteService.deleteFavoriteById(favoritePrimaryKey);
        return ResponseEntity.noContent().build();
    }
}
