package com.project.house.rental.mapper;

import com.project.house.rental.dto.FavoriteDto;
import com.project.house.rental.entity.Favorite;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(FavoriteMapperDecorator.class)
public interface FavoriteMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "propertyTitle")
    FavoriteDto toDto(Favorite favorite);

    Favorite toEntity(FavoriteDto favoriteDto);
}