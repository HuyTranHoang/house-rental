package com.project.house.rental.service;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.entity.Amenity;

import java.util.Map;

public interface AmenityService extends GenericService<Amenity, AmenityDto> {
    Map<String, Object> getAllAmenitiesWithParams(AmenityParams amenityParams);
}
