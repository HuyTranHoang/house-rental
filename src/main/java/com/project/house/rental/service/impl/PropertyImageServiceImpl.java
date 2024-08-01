package com.project.house.rental.service.impl;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.PropertyImage;
import com.project.house.rental.repository.PropertyImageRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.service.PropertyImageService;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;
    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final HibernateFilterHelper hibernateFilterHelper;

    public PropertyImageServiceImpl(PropertyImageRepository propertyImageRepository, PropertyRepository propertyRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper) {
        this.propertyImageRepository = propertyImageRepository;
        this.propertyRepository = propertyRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<PropertyImageDto> findByPropertyId(long id) {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);
        List<PropertyImage> propertyImages = propertyImageRepository.findAllByPropertyId(id);
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);

        return propertyImages.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void deleteByPropertyId(long id) throws IOException {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);
        List<PropertyImage> propertyImages = propertyImageRepository.findAllByPropertyId(id);
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);

        for (PropertyImage propertyImage : propertyImages) {
            cloudinaryService.delete(propertyImage.getPublicId());
            propertyImageRepository.deleteById(propertyImage.getId());
        }
    }

    @Override
    public PropertyImageDto toDto(PropertyImage propertyImage) {
        return PropertyImageDto.builder()
                .id(propertyImage.getId())
                .imageUrl(propertyImage.getImageUrl())
                .build();
    }

    @Override
    public PropertyImage toEntity(PropertyImageDto propertyImageDto) {
        Property property = propertyRepository.findById(propertyImageDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy property với id: " + propertyImageDto.getPropertyId()));

        return PropertyImage.builder()
                .imageUrl(propertyImageDto.getImageUrl())
                .property(property)
                .build();
    }

    @Override
    public void updateEntityFromDto(PropertyImage propertyImage, PropertyImageDto propertyImageDto) {
        propertyImage.setImageUrl(propertyImageDto.getImageUrl());
    }
}
