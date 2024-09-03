package com.project.house.rental.mapper;

import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.PropertyImage;
import com.project.house.rental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyImageMapperDecorator implements PropertyImageMapper {

    @Autowired
    @Qualifier("delegate")
    private PropertyImageMapper delegate;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public PropertyImage toEntity(PropertyImageDto propertyImageDto) {
        PropertyImage propertyImage = delegate.toEntity(propertyImageDto);

        propertyRepository.findById(propertyImageDto.getPropertyId())
                .ifPresent(propertyImage::setProperty);

        return propertyImage;
    }
}
