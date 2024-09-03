package com.project.house.rental.mapper;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityDto toDto(City city);

    City toEntity(CityDto cityDto);

    void updateFromDto(CityDto cityDto, @MappingTarget City city);
}
