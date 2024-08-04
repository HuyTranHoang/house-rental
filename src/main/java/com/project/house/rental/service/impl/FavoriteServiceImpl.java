package com.project.house.rental.service.impl;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import com.project.house.rental.repository.FavoriteRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.FavoriteService;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserRepository userRepository, PropertyRepository propertyRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<FavoriteDto> getAllFavorites() {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_FAVORITE_FILTER);

        List<Favorite> favorites = favoriteRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_FAVORITE_FILTER);

        return favorites.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public FavoriteDto getFavoriteById(FavoritePrimaryKey favoritePrimaryKey) {
        Favorite favorite = favoriteRepository.findByCompositeKeyWithFilter(favoritePrimaryKey);

        if (favorite == null) {
            throw new NoResultException("Không tìm thấy yêu thích với id: " + favoritePrimaryKey);
        }

        return toDto(favorite);
    }

    @Override
    public List<FavoriteDto> getFavoriteByUserId(long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserIdWithFilter(userId);

        if (favorites.isEmpty()) {
            throw new NoResultException("Không tìm thấy yêu thích với id người dùng: " + userId);
        }

        return favorites.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<FavoriteDto> getFavoriteByPropertyId(long propertyId) {
        List<Favorite> favorites = favoriteRepository.findByPropertyIdWithFilter(propertyId);

        if (favorites.isEmpty()) {
            throw new NoResultException("Không tìm thấy yêu thích với id bài đăng: " + propertyId);
        }

        return favorites.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public FavoriteDto createFavorite(FavoriteDto favoriteDto) {
        Favorite favorite = toEntity(favoriteDto);
        favorite = favoriteRepository.save(favorite);
        return toDto(favorite);
    }

    @Override
    public void deleteFavoriteById(FavoritePrimaryKey favoritePrimaryKey) {
        Favorite favorite = favoriteRepository.findByCompositeKeyWithFilter(favoritePrimaryKey);

        if (favorite == null) {
            throw new NoResultException("Không tìm thấy yêu thích với id: " + favoritePrimaryKey);
        }

        favorite.setDeleted(true);
        favoriteRepository.save(favorite);
    }

    @Override
    public FavoriteDto toDto(Favorite favorite) {
        return FavoriteDto.builder()
                .userId(favorite.getUser().getId())
                .username(favorite.getUser().getUsername())
                .propertyId(favorite.getProperty().getId())
                .propertyTitle(favorite.getProperty().getTitle())
                .build();
    }

    @Override
    public Favorite toEntity(FavoriteDto favoriteDto) {
        UserEntity userEntity = userRepository.findById(favoriteDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + favoriteDto.getUserId()));

        Property property = propertyRepository.findById(favoriteDto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng với id: " + favoriteDto.getPropertyId()));

        FavoritePrimaryKey favoritePrimaryKey = FavoritePrimaryKey.builder()
                .userId(favoriteDto.getUserId())
                .propertyId(favoriteDto.getPropertyId())
                .build();

        return Favorite.builder()
                .favoritePrimaryKey(favoritePrimaryKey)
                .user(userEntity)
                .property(property)
                .build();
    }
}
