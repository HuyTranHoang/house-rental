package com.project.house.rental.mapper;

import com.project.house.rental.dto.AdvertisementDto;
import com.project.house.rental.entity.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AdvertisementMapper {
    AdvertisementMapper INSTANCE = Mappers.getMapper(AdvertisementMapper.class);

    AdvertisementDto toDto(Advertisement advertisement);

    Advertisement toEntity(AdvertisementDto advertisementDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(AdvertisementDto advertisementDto, @MappingTarget Advertisement advertisement);
}
