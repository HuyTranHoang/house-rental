package com.project.house.rental.service.impl;


import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.*;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.service.PropertyService;
import com.project.house.rental.specification.PropertySpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;
    private final AmenityRepository amenityRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final CloudinaryService cloudinaryService;
    private final HibernateFilterHelper hibernateFilterHelper;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CityRepository cityRepository, RoomTypeRepository roomTypeRepository, UserRepository userRepository, DistrictRepository districtRepository, AmenityRepository amenityRepository, PropertyImageRepository propertyImageRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper) {
        this.propertyRepository = propertyRepository;
        this.cityRepository = cityRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.userRepository = userRepository;
        this.districtRepository = districtRepository;
        this.amenityRepository = amenityRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<PropertyDto> getAllPropertiesForFilter(PropertyParams propertyParams) {
        Specification<Property> spec = Specification
                .where(PropertySpecification.filterByCriteria(propertyParams.getFilter()))
                .and(PropertySpecification.searchByDistrictName(propertyParams.getDistrictName()))
                .and(PropertySpecification.searchByCityName(propertyParams.getCityName()))
                .and(PropertySpecification.searchByPrice(propertyParams.getPrice()))
                .and(PropertySpecification.searchByRoomTypeName(propertyParams.getRoomTypeName()))
                .and(PropertySpecification.searchByArea(propertyParams.getArea()));

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        List<Property> properties = propertyRepository.findAll(spec);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        return properties.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<PropertyDto> getAllProperties() {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        List<Property> properties = propertyRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        return properties.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PropertyDto getPropertyById(long id) {
        Property property = propertyRepository.findByIdWithFilter(id);

        if (property == null) {
            throw new NoResultException("Không tìm thấy tin đăng với id: " + id);
        }

        return toDto(property);
    }

    @Override
    public PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images) throws IOException {
        Property property = toEntity(propertyDto);
        property = propertyRepository.save(property);

        List<PropertyImage> propertyImages = new ArrayList<>();

        if (images != null && images.length > 0) {
            Map<String, String> cloudinaryResponse = cloudinaryService.uploadImages(images);

            for (Map.Entry<String, String> entry : cloudinaryResponse.entrySet()) {
                PropertyImage propertyImage = PropertyImage.builder()
                        .imageUrl(entry.getValue())
                        .publicId(entry.getKey())
                        .property(property)
                        .build();
                propertyImages.add(propertyImage);
            }
            propertyImageRepository.saveAll(propertyImages);
        }

        property.setPropertyImages(propertyImages);

        return toDto(property);
    }

    @Override
    public PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) throws IOException {
        Property property = propertyRepository.findByIdWithFilter(id);

        if (property == null) {
            throw new NoResultException("Không tìm thấy tin đăng với id: " + id);
        }

        if (images != null && images.length > 0) {
            Map<String, String> cloudinaryResponse = cloudinaryService.uploadImages(images);

            for (Map.Entry<String, String> entry : cloudinaryResponse.entrySet()) {
                PropertyImage propertyImage = PropertyImage.builder()
                        .imageUrl(entry.getValue())
                        .publicId(entry.getKey())
                        .property(property)
                        .build();
                property.getPropertyImages().add(propertyImage);
            }
            propertyImageRepository.saveAll(property.getPropertyImages());
        }


        updateEntityFromDto(property, propertyDto);

        propertyRepository.save(property);

        return toDto(property);
    }

    @Override
    public void deletePropertyById(long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy tin đăng với id: " + id));

        propertyRepository.deleteById(property.getId());
    }

    @Override
    public PropertyDto toDto(Property property) {
        List<String> amenities = property.getAmenities()
                .stream()
                .map(Amenity::getName)
                .toList();

        List<String> propertyImages = property.getPropertyImages().stream()
                .filter(image -> !image.isDeleted())
                .map(PropertyImage::getImageUrl)
                .toList();

        return PropertyDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price((property.getPrice()))
                .location(property.getLocation())
                .numRooms(property.getNumRooms())
                .status(String.valueOf(property.getStatus()))
                .isBlocked(property.isBlocked())
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
                .propertyImages(propertyImages)
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

        List<PropertyImage> propertyImages = new ArrayList<>();

        if (propertyDto.getPropertyImages() != null) {
            propertyImages = propertyDto.getPropertyImages().stream()
                    .map(propertyImageRepository::findByImageUrl)
                    .toList();
        }

        if (!isValidPropertyStatus(propertyDto.getStatus())) {
            throw new IllegalArgumentException("Trạng thái [" + propertyDto.getStatus() + "] không hợp lệ!");
        }

        return Property.builder()
                .title(propertyDto.getTitle())
                .description(propertyDto.getDescription())
                .numRooms(propertyDto.getNumRooms())
                .location(propertyDto.getLocation())
                .status(Property.PropertyStatus.valueOf(propertyDto.getStatus()))
                .isBlocked(propertyDto.isBlocked())
                .price(propertyDto.getPrice())
                .area(propertyDto.getArea())
                .city(city)
                .roomType(roomType)
                .user(user)
                .district(district)
                .amenities(amenities)
                .propertyImages(propertyImages)
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

        List<Amenity> amenities = new ArrayList<>();
        if (propertyDto.getAmenities() != null) {
            amenities = propertyDto.getAmenities().stream()
                    .map(amenityRepository::findByNameIgnoreCase)
                    .toList();
        }


//        List<PropertyImage> propertyImages = new ArrayList<>();
//        if (propertyDto.getPropertyImages() != null) {
//            propertyImages = propertyDto.getPropertyImages().stream()
//                    .map(propertyImageRepository::findByImageUrl)
//                    .toList();
//        }

        if (!isValidPropertyStatus(propertyDto.getStatus())) {
            throw new IllegalArgumentException("Trạng thái [" + propertyDto.getStatus() + "] không hợp lệ!");
        }

        property.setTitle(propertyDto.getTitle());
        property.setDescription(propertyDto.getDescription());
        property.setArea(propertyDto.getArea());
        property.setStatus(Property.PropertyStatus.valueOf(propertyDto.getStatus()));
        property.setBlocked(propertyDto.isBlocked());
        property.setLocation(propertyDto.getLocation());
        property.setPrice(propertyDto.getPrice());
        property.setNumRooms(propertyDto.getNumRooms());
        property.setCity(city);
        property.setDistrict(district);
        property.setUser(user);
        property.setRoomType(roomType);
        property.setAmenities(new ArrayList<>(amenities));
//        property.setPropertyImages(propertyImages);
    }

    @Override
    public Map<String, Object> getAllPropertiesWithParams(PropertyParams propertyParams) {
        Specification<Property> spec = Specification
                .where(PropertySpecification.searchByDistrictName(propertyParams.getDistrictName()))
                .and(PropertySpecification.searchByCityName(propertyParams.getCityName()))
                .and(PropertySpecification.searchByPrice(propertyParams.getPrice()))
                .and(PropertySpecification.searchByRoomTypeName(propertyParams.getRoomTypeName()))
                .and(PropertySpecification.searchByArea(propertyParams.getArea()));

        Sort sort = switch (propertyParams.getSortBy()) {
            case "priceDesc" -> Sort.by(Property_.PRICE).descending();
            case "priceAsc" -> Sort.by(Property_.PRICE);
            case "areaDesc" -> Sort.by(Property_.AREA).descending();
            case "areaAsc" -> Sort.by(Property_.AREA);
            case "createdAtAsc" -> Sort.by(Property_.CREATED_AT);
            default -> Sort.by(Property_.CREATED_AT).descending();
        };

        if (propertyParams.getPageNumber() < 0) {
            propertyParams.setPageNumber(0);
        }

        if (propertyParams.getPageSize() <= 0) {
            propertyParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                propertyParams.getPageNumber(),
                propertyParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        Page<Property> propertyPage = propertyRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        List<PropertyDto> propertyDtoList = propertyPage.stream()
                .map(this::toDto)
                .toList();

        PageInfo pageInfo = new PageInfo(
                propertyPage.getTotalPages(),
                propertyPage.getTotalElements(),
                propertyPage.getNumber(),
                propertyPage.getSize()
        );

        return Map.of(
                "pageInfo", pageInfo,
                "data", propertyDtoList
        );
    }


    public static boolean isValidPropertyStatus(String status) {
        try {
            Property.PropertyStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


}
