package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.PropertyImage;

import java.io.IOException;
import java.util.List;

public interface PropertyImageService {
    List<PropertyImageDto> findByPropertyId(long id);

    void deleteByPropertyId(long id) throws IOException;

    PropertyImageDto toDto(PropertyImage propertyImage);

    PropertyImage toEntity(PropertyImageDto propertyImageDto);

    void updateEntityFromDto(PropertyImage propertyImage, PropertyImageDto propertyImageDto);
}
