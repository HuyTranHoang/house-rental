package com.project.house.rental.service;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PropertyService extends GenericService<Property, PropertyDto> {

    PropertyDto create(PropertyDto propertyDto, MultipartFile[] images) throws IOException;
}
