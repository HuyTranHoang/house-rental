package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.dto.params.FavoriteParams;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Favorite_;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.FavoriteRepository;
import com.project.house.rental.repository.GenericRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.FavoriteService;
import com.project.house.rental.specification.FavoriteSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FavoriteServiceImpl extends GenericServiceImpl<Favorite, FavoriteDto> implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserRepository userRepository, PropertyRepository propertyRepository, JWTTokenProvider jwtTokenProvider) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected GenericRepository<Favorite> getRepository() {
        return favoriteRepository;
    }

    @Override
    public Map<String, Object> getAllFavoritesWithParams(FavoriteParams favoriteParams) {
        Specification<Favorite> spec = FavoriteSpecification.filterByUserId(favoriteParams.getUserId())
                .and(FavoriteSpecification.filterByPropertyId(favoriteParams.getPropertyId()));

        if (!StringUtils.hasLength(favoriteParams.getSortBy())) {
            favoriteParams.setSortBy("createdAtDesc");
        }

        Sort sort = switch (favoriteParams.getSortBy()) {
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

        Page<Favorite> cityPage = favoriteRepository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(
                cityPage.getNumber(),
                cityPage.getTotalElements(),
                cityPage.getTotalPages(),
                cityPage.getSize()
        );

        List<FavoriteDto> favoriteDtoList = cityPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", favoriteDtoList
        );
    }

    @Override
    public FavoriteDto create(FavoriteDto favoriteDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để thay đổi ảnh đại diện!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        favoriteDto.setUserId(user.getId());

        return super.create(favoriteDto);
    }

    @Override
    public FavoriteDto toDto(Favorite favorite) {
        return FavoriteDto.builder()
                .userId(favorite.getUser().getId())
                .userName(favorite.getUser().getUsername())
                .propertyId(favorite.getProperty().getId())
                .propertyTitle(favorite.getProperty().getTitle())
                .build();
    }

    @Override
    public Favorite toEntity(FavoriteDto favoriteDto) {
        UserEntity user = userRepository.findById(favoriteDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Property property = propertyRepository.findById(favoriteDto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found!"));

        return Favorite.builder()
                .user(user)
                .property(property)
                .build();
    }

    @Override
    public void updateEntityFromDto(Favorite favorite, FavoriteDto favoriteDto) {

    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return jwtTokenProvider.getSubject(token);
        }
        return null;
    }
}
