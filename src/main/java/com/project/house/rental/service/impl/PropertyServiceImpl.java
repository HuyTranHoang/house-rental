package com.project.house.rental.service.impl;


import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.District;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.RoomType;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.repository.DistrictRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.RoomTypeRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.PropertyService;
import com.project.house.rental.specification.PropertySpecification;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PropertyServiceImpl  extends GenericServiceImpl<Property, PropertyDto> implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CityRepository cityRepository, RoomTypeRepository roomTypeRepository, UserRepository userRepository, DistrictRepository districtRepository) {
        this.propertyRepository = propertyRepository;
        this.cityRepository = cityRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.userRepository = userRepository;
        this.districtRepository = districtRepository;
    }
    @Override
    protected PropertyRepository getRepository() {
        return propertyRepository;
    }

    @Override
    public PropertyDto toDto(Property property) {
        return PropertyDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price((property.getPrice()))
                .location(property.getLocation())
                .numRooms(property.getNumRooms())
                .status(String.valueOf(property.getStatus()))
                .area((property.getArea()))
                .cityName(property.getCity().getName())
                .cityId(property.getCity().getId())
                .roomTypeName(property.getRoomType().getName())
                .roomTypeId(property.getRoomType().getId())
                .userName(property.getUser().getUsername())
                .userId(property.getUser().getId())
                .districtName(property.getDistrict().getName())
                .districtId(property.getDistrict().getId())
                .build();
    }

    @Override
    public Property toEntity(PropertyDto propertyDto) {

        City city = cityRepository.findById(propertyDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id thành phố : " + propertyDto.getCityId()));
        RoomType roomType = roomTypeRepository.findById(propertyDto.getRoomTypeId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id loại phòng: " + propertyDto.getRoomTypeId()));
        UserEntity user = userRepository.findById(propertyDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id user: " + propertyDto.getUserId()));
        District district = districtRepository.findById(propertyDto.getDistrictId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy id Quận: " + propertyDto.getDistrictId()));

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
    }

    @Override
    public Map<String, Object> getAllPropertiesWithParams(Property propertyParams) {
        List<Property> properties = propertyRepository.findAll(PropertySpecification.searchByCriteria(propertyParams));
        List<PropertyDto> propertyDtos = properties.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return Map.of(
                "total", properties.size(),
                "properties", propertyDtos
        );
    }

    @Override
    public List<PropertyDto> getAllWithFilter(String filter) {
        List<Property> properties = propertyRepository.findAll(PropertySpecification.filterByCriteria(filter));
        return properties.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
