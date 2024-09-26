package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface PropertyService {

    PropertyDto getPropertyById(long id);

    PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images) throws IOException;

    PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) throws IOException;

    void deletePropertyById(long id);

    Map<String, Object> getAllPropertiesWithParams(PropertyParams propertyParams);

    PropertyDto blockProperty(long id, String status);

    PropertyDto updatePropertyStatus(long id, String status);

    PropertyDto hideProperty(long id, HttpServletRequest request);
}
