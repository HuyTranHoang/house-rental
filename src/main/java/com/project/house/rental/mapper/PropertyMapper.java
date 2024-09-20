package com.project.house.rental.mapper;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.entity.Property;
import org.mapstruct.*;


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
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "propertyImages", ignore = true)
    PropertyDto toDto(Property property);

    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "propertyImages", ignore = true)
    Property toEntity(PropertyDto propertyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "propertyImages", ignore = true)
    void updateEntityFromDto(PropertyDto propertyDto, @MappingTarget Property property);
}