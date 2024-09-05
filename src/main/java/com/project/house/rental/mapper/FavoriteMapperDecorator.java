package com.project.house.rental.mapper;


import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

public abstract class FavoriteMapperDecorator implements FavoriteMapper {

    @Autowired
    @Qualifier("delegate")
    private FavoriteMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Favorite toEntity(FavoriteDto favoriteDto) {
        Favorite favorite = delegate.toEntity(favoriteDto);

        UserEntity userEntity = userRepository.findById(favoriteDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy người dùng với id: " + favoriteDto.getUserId()));

        Property property = propertyRepository.findById(favoriteDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng với id: " + favoriteDto.getPropertyId()));

        FavoritePrimaryKey favoritePrimaryKey = FavoritePrimaryKey.builder()
                .userId(userEntity.getId())
                .propertyId(property.getId())
                .build();

        favorite.setFavoritePrimaryKey(favoritePrimaryKey);
        favorite.setUser(userEntity);
        favorite.setProperty(property);
        favorite.setCreatedAt(new Date());

        return favorite;
    }

}