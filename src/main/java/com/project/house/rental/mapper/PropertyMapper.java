package com.project.house.rental.mapper;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.PropertyImageBlurhashDto;
import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.entity.Amenity;



@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(PropertyMapperDecorator.class)
public interface PropertyMapper {

    @Mapping(source = "blocked", target = "isBlocked")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "roomType.id", target = "roomTypeId")
    @Mapping(source = "roomType.name", target = "roomTypeName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "districtName")
    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "amenityName")
    @Mapping(source = "propertyImages", target = "propertyImages", qualifiedByName = "imageUrl")
    @Mapping(source = "priority", target = "isPriority")
    @Mapping(source = "priorityExpiration", target = "priorityExpiration")
    PropertyDto toDto(Property property);

    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "propertyImages", ignore = true)
    Property toEntity(PropertyDto propertyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "propertyImages", ignore = true)
    void updateEntityFromDto(PropertyDto propertyDto, @MappingTarget Property property);

    @Named("amenityName")
    public static List<String> amenityName(List<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::getName)
                .toList();
    }

    @Named("imageUrl")
    public static List<PropertyImageBlurhashDto> imageUrl(List<PropertyImage> propertyImages) {
        return propertyImages.stream()
                .filter(image -> !image.isDeleted())
                .map(image -> PropertyImageBlurhashDto.builder()
                        .imageUrl(image.getImageUrl())
                        .blurhash(image.getBlurhash())
                        .build())
                .toList();
    }
}