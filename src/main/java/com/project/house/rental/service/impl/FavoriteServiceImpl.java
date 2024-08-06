package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import com.project.house.rental.repository.FavoriteRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.FavoriteService;
import com.project.house.rental.specification.FavoriteSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final JWTTokenProvider jwtTokenProvider;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserRepository userRepository, PropertyRepository propertyRepository, HibernateFilterHelper hibernateFilterHelper, JWTTokenProvider jwtTokenProvider) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public FavoriteDto createFavorite(FavoriteDto favoriteDto, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request);

        if (username == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new RuntimeException("Không tìm thấy người dùng với username: " + username);
        }

        favoriteDto.setUserId(currentUser.getId());

        Favorite existingFavorite = favoriteRepository.findByCompositeKey(FavoritePrimaryKey.builder()
                .userId(favoriteDto.getUserId())
                .propertyId(favoriteDto.getPropertyId())
                .build());

        if (existingFavorite != null && !existingFavorite.isDeleted()) {
            existingFavorite.setDeleted(false);
            favoriteRepository.save(existingFavorite);
            return toDto(existingFavorite);
        }

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
                .createdAt(favorite.getCreatedAt())
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

    @Override
    public Map<String, Object> getAllFavoritesWithParams(FavoriteParams favoriteParams) {
        Specification<Favorite> spec = FavoriteSpecification.filterByUserId(favoriteParams.getUserId())
                .and(FavoriteSpecification.filterByPropertyId(favoriteParams.getPropertyId()))
                .and(FavoriteSpecification.searchByPropertyTitle(favoriteParams.getPropertyTitle()));

        Sort sort = switch (favoriteParams.getSortBy()) {
            case "propertyPriceAsc" -> Sort.by(Favorite_.PROPERTY + "." + Property_.PRICE).ascending();
            case "propertyPriceDesc" -> Sort.by(Favorite_.PROPERTY + "." + Property_.PRICE).descending();
            case "createdAtAsc" -> Sort.by(Favorite_.CREATED_AT);
            default -> Sort.by(Favorite_.CREATED_AT).descending();
        };

        if (favoriteParams.getPageNumber() < 0) {
            favoriteParams.setPageNumber(0);
        }

        if (favoriteParams.getPageSize() <= 0) {
            favoriteParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                favoriteParams.getPageNumber(),
                favoriteParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_FAVORITE_FILTER);

        Page<Favorite> cityPage = favoriteRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_FAVORITE_FILTER);

        PageInfo pageInfo = new PageInfo(cityPage);

        List<FavoriteDto> favoriteDtoList = cityPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", favoriteDtoList
        );
    }
}
