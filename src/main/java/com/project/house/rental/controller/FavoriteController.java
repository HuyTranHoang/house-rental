package com.project.house.rental.controller;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.FavoriteService;
import com.project.house.rental.service.PropertyService;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService, UserService userService, PropertyService propertyService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllFavoritesWithParams(@ModelAttribute FavoriteParams favoriteParams) {
        Map<String, Object> favoritesWithParams = favoriteService.getAllFavoritesWithParams(favoriteParams);
        return ResponseEntity.ok(favoritesWithParams);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FavoriteDto>> getAllFavorites() {
        List<FavoriteDto> favoriteDtoList = favoriteService.getAll();

        return ResponseEntity.ok(favoriteDtoList);
    }

    //
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDto> getFavoriteById(@PathVariable Long id) {
        FavoriteDto favoriteDto = favoriteService.getById(id);
        return ResponseEntity.ok(favoriteDto);
    }

    //
    @PostMapping({"", "/"})
    public ResponseEntity<FavoriteDto> addFavorite(@RequestBody FavoriteDto favoriteDto, HttpServletRequest request) throws CustomRuntimeException {
        FavoriteDto newFavorite = favoriteService.create(favoriteDto, request);
        return ResponseEntity.ok(newFavorite);
    }

    //
    @PutMapping("/{id}")
    public ResponseEntity<FavoriteDto> updateFavorite(@PathVariable Long id, @RequestBody FavoriteDto favoriteDto) {
        FavoriteDto updatedFavorite = favoriteService.update(id, favoriteDto);
        return ResponseEntity.ok(updatedFavorite);
    }

    //
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
