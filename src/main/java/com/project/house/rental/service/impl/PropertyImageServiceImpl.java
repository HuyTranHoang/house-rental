package com.project.house.rental.service.impl;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.PropertyImage;
import com.project.house.rental.mapper.PropertyImageMapper;
import com.project.house.rental.repository.PropertyImageRepository;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.service.PropertyImageService;
import com.project.house.rental.utils.HibernateFilterHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;
    private final CloudinaryService cloudinaryService;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final PropertyImageMapper propertyImageMapper;

    public PropertyImageServiceImpl(PropertyImageRepository propertyImageRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper, PropertyImageMapper propertyImageMapper) {
        this.propertyImageRepository = propertyImageRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.propertyImageMapper = propertyImageMapper;
    }

    @Override
    public List<PropertyImageDto> findByPropertyId(long id) {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);
        List<PropertyImage> propertyImages = propertyImageRepository.findAllByPropertyId(id);
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_IMAGE_FILTER);

        return propertyImages.stream()
                .map(propertyImageMapper::toDto)
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
    public void deleteById(long id) throws IOException {
        PropertyImage propertyImage = propertyImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property image not found"));

        cloudinaryService.delete(propertyImage.getPublicId());
        propertyImageRepository.deleteById(id);
    }
}
