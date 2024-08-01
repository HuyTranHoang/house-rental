package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import com.project.house.rental.entity.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PropertyService {
    
    List<PropertyDto> getAllProperties();

    PropertyDto getPropertyById(long id);

    PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images) throws IOException;

    PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) throws IOException;

    void deletePropertyById(long id);

    PropertyDto toDto(Property property);

    Property toEntity(PropertyDto propertyDto);

    void updateEntityFromDto(Property property, PropertyDto propertyDto);

    Map<String, Object> getAllPropertiesWithParams(PropertyParams propertyParams);
}
