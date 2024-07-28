package com.project.house.rental.service.impl;


import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.*;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.PropertyService;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PropertyServiceImpl extends GenericServiceImpl<Property, PropertyDto> implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;
    private final AmenityRepository amenityRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CityRepository cityRepository, RoomTypeRepository roomTypeRepository, UserRepository userRepository, DistrictRepository districtRepository, AmenityRepository amenityRepository) {
        this.propertyRepository = propertyRepository;
        this.cityRepository = cityRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.userRepository = userRepository;
        this.districtRepository = districtRepository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    protected PropertyRepository getRepository() {
        return propertyRepository;
    }

    @Override
    public PropertyDto toDto(Property property) {
        List<String> amenities = property.getAmenities()
                .stream()
                .map(Amenity::getName)
                .toList();

        return PropertyDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price((property.getPrice()))
                .location(property.getLocation())
                .numRooms(property.getNumRooms())
                .status(String.valueOf(property.getStatus()))
                .area((property.getArea()))
                .cityId(property.getCity().getId())
                .cityName(property.getCity().getName())
                .roomTypeId(property.getRoomType().getId())
                .roomTypeName(property.getRoomType().getName())
                .userId(property.getUser().getId())
                .userName(property.getUser().getUsername())
                .districtId(property.getDistrict().getId())
                .districtName(property.getDistrict().getName())
                .amenities(amenities)
                .build();
    }

    @Override
    public Property toEntity(PropertyDto propertyDto) {
        City city = cityRepository.findById(propertyDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id thành phố: " + propertyDto.getCityId()));
        RoomType roomType = roomTypeRepository.findById(propertyDto.getRoomTypeId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id loại phòng: " + propertyDto.getRoomTypeId()));
        UserEntity user = userRepository.findById(propertyDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id user: " + propertyDto.getUserId()));
        District district = districtRepository.findById(propertyDto.getDistrictId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id Quận: " + propertyDto.getDistrictId()));

        List<Amenity> amenities = propertyDto.getAmenities().stream()
                .map(amenityRepository::findByNameIgnoreCase)
                .toList();

        if (!propertyDto.getStatus().equals("PENDING") && !propertyDto.getStatus().equals("RESOLVED")) {
            throw new NoResultException("Status phải là PENDING hoặc RESOLVED");
        }


        return Property.builder()
                .title(propertyDto.getTitle())
                .description(propertyDto.getDescription())
                .numRooms(propertyDto.getNumRooms())
                .location(propertyDto.getLocation())
                .status(Property.PropertyStatus.valueOf(propertyDto.getStatus()))
                .price(propertyDto.getPrice())
                .area(propertyDto.getArea())
                .city(city)
                .roomType(roomType)
                .user(user)
                .district(district)
                .amenities(amenities)
                .build();
    }

    @Override
    public void updateEntityFromDto(Property property, PropertyDto propertyDto) {

        City city = cityRepository.findById(propertyDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id thành phố: " + propertyDto.getCityId()));
        RoomType roomType = roomTypeRepository.findById(propertyDto.getRoomTypeId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id loại phòng: " + propertyDto.getRoomTypeId()));
        UserEntity user = userRepository.findById(propertyDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id user: " + propertyDto.getUserId()));
        District district = districtRepository.findById(propertyDto.getDistrictId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id Quận: " + propertyDto.getDistrictId()));

        List<Amenity> amenities = propertyDto.getAmenities().stream()
                .map(amenityRepository::findByNameIgnoreCase)
                .toList();

        if (!propertyDto.getStatus().equals("PENDING") && !propertyDto.getStatus().equals("RESOLVED")) {
            throw new NoResultException("Status phải là PENDING hoặc RESOLVED");
        }

        property.setTitle(propertyDto.getTitle());
        property.setDescription(propertyDto.getDescription());
        property.setArea(propertyDto.getArea());
        property.setStatus(Property.PropertyStatus.valueOf(propertyDto.getStatus()));
        property.setLocation(propertyDto.getLocation());
        property.setPrice(propertyDto.getPrice());
        property.setNumRooms(propertyDto.getNumRooms());
        property.setCity(city);
        property.setDistrict(district);
        property.setUser(user);
        property.setRoomType(roomType);
        property.setAmenities(amenities);
    }
}
