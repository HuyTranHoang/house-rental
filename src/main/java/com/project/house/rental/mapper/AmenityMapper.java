package com.project.house.rental.mapper;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.entity.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AmenityMapper {

    AmenityMapper INSTANCE = Mappers.getMapper(AmenityMapper.class);

    AmenityDto toDto(Amenity amenity);

    Amenity toEntity(AmenityDto amenityDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(AmenityDto amenityDto, @MappingTarget Amenity amenity);
}
