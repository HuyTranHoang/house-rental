package com.project.house.rental.mapper;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.entity.District;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(DistrictMapperDecorator.class)
public interface DistrictMapper {
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    DistrictDto toDto(District district);

    District toEntity(DistrictDto districtDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(DistrictDto districtDto, @MappingTarget District district);
}
