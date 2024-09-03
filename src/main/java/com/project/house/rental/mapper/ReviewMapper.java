package com.project.house.rental.mapper;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.entity.Review;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(ReviewMapperDecorator.class)
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "user.avatarUrl", target = "userAvatar")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "propertyTitle")
    ReviewDto toDto(Review review);

    Review toEntity(ReviewDto reviewDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(ReviewDto reviewDto, @MappingTarget Review review);
}