package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PropertyService {

    PropertyDto getPropertyById(long id);

    PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images, MultipartFile thumbnailImage) throws IOException;

    PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) throws IOException;

    void deletePropertyById(long id);

    void selfDeletePropertyById(long id, HttpServletRequest request);

    Map<String, Object> getAllPropertiesWithParams(PropertyParams propertyParams);

    PropertyDto blockProperty(long id, String status);

    PropertyDto updatePropertyStatus(long id, String status);

    PropertyDto hideProperty(long id, HttpServletRequest request);

    PropertyDto refreshProperty(long id, HttpServletRequest request);

    PropertyDto prioritizeProperty(long id, HttpServletRequest request);

    List<PropertyDto> getPriorityProperties();

    List<PropertyDto> getRelatedProperties(long propertyId);
}
