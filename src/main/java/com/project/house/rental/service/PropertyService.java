package com.project.house.rental.service;
import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.Property;

import java.util.List;
import java.util.Map;

public interface PropertyService extends GenericService<Property, PropertyDto> {
    Map<String, Object> getAllPropertiesWithParams(Property propertyParams);

    List<PropertyDto> getAllWithFilter(String filter);
}
