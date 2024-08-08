package com.project.house.rental.service;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.entity.Amenity;

import java.util.List;
import java.util.Map;

public interface AmenityService {

    List<AmenityDto> getAllAmenities();

    AmenityDto getAmenityById(long id);

    AmenityDto createAmenity(AmenityDto amenityDto);

    AmenityDto updateAmenity(long id, AmenityDto amenityDto);

    void deleteAmenityById(long id);
    void deleteMultipleAmenities(List<Long> ids);

    Map<String, Object> getAllAmenitiesWithParams(AmenityParams amenityParams);

    AmenityDto toDto(Amenity amenity);

    Amenity toEntity(AmenityDto amenityDto);

    void updateEntityFromDto(Amenity amenity, AmenityDto amenityDto);
}
