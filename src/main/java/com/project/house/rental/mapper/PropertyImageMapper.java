package com.project.house.rental.mapper;

import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.PropertyImage;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(PropertyImageMapperDecorator.class)
public interface PropertyImageMapper {
    PropertyImageDto toDto(PropertyImage propertyImage);

    PropertyImage toEntity(PropertyImageDto propertyImageDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(PropertyImageDto propertyImageDto, @MappingTarget PropertyImage propertyImage);
}