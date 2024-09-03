package com.project.house.rental.mapper;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.repository.DistrictRepository;
import com.project.house.rental.repository.PropertyImageRepository;
import com.project.house.rental.repository.RoomTypeRepository;
import com.project.house.rental.repository.AmenityRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PropertyMapperDecorator implements PropertyMapper {

    @Autowired
    @Qualifier("delegate")
    private PropertyMapper delegate;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @Override
    public Property toEntity(PropertyDto propertyDto) {
        Property property = delegate.toEntity(propertyDto);

        City city = cityRepository.findById(propertyDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id thành phố: " + propertyDto.getCityId()));
        RoomType roomType = roomTypeRepository.findById(propertyDto.getRoomTypeId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id loại phòng: " + propertyDto.getRoomTypeId()));
        UserEntity user = userRepository.findById(propertyDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id user: " + propertyDto.getUserId()));
        District district = districtRepository.findById(propertyDto.getDistrictId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id Quận: " + propertyDto.getDistrictId()));

        List<Amenity> amenities = new ArrayList<>();

        if (propertyDto.getAmenities() != null) {
            amenities = propertyDto.getAmenities().stream()
                    .map(amenityRepository::findByNameIgnoreCase)
                    .toList();
        }

        List<PropertyImage> propertyImages = new ArrayList<>();

        if (propertyDto.getPropertyImages() != null) {
            propertyImages = propertyDto.getPropertyImages().stream()
                    .map(propertyImageRepository::findByImageUrl)
                    .toList();
        }

        if (!isValidPropertyStatus(propertyDto.getStatus())) {
            throw new IllegalArgumentException("Trạng thái [" + propertyDto.getStatus() + "] không hợp lệ!");
        }

        property.setCity(city);
        property.setRoomType(roomType);
        property.setUser(user);
        property.setDistrict(district);

        property.getAmenities().clear();
        property.getAmenities().addAll(amenities);

        property.getPropertyImages().clear();
        property.getPropertyImages().addAll(propertyImages);

        return property;
    }

    @Override
    public void updateEntityFromDto(PropertyDto propertyDto, @MappingTarget Property property) {
        delegate.updateEntityFromDto(propertyDto, property);

        City city = cityRepository.findById(propertyDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id thành phố: " + propertyDto.getCityId()));
        RoomType roomType = roomTypeRepository.findById(propertyDto.getRoomTypeId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id loại phòng: " + propertyDto.getRoomTypeId()));
        UserEntity user = userRepository.findById(propertyDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id user: " + propertyDto.getUserId()));
        District district = districtRepository.findById(propertyDto.getDistrictId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id Quận: " + propertyDto.getDistrictId()));

        List<Amenity> amenities = new ArrayList<>();

        if (propertyDto.getAmenities() != null) {
            amenities = propertyDto.getAmenities().stream()
                    .map(amenityRepository::findByNameIgnoreCase)
                    .toList();
        }

        if (!isValidPropertyStatus(propertyDto.getStatus())) {
            throw new IllegalArgumentException("Trạng thái [" + propertyDto.getStatus() + "] không hợp lệ!");
        }

        property.setCity(city);
        property.setDistrict(district);
        property.setUser(user);
        property.setRoomType(roomType);

        property.getAmenities().clear();
        property.getAmenities().addAll(amenities);
    }

    private boolean isValidPropertyStatus(String status) {
        try {
            Property.PropertyStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}