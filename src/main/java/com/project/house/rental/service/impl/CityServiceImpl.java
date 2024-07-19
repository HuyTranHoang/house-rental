package com.project.house.rental.service.impl;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.entity.City;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.service.CityService;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl extends GenericServiceImpl<City, CityDto> implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityRepository getRepository() {
        return cityRepository;
    }

    @Override
    public CityDto toDto(City city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    @Override
    public City toEntity(CityDto cityDto) {
        return City.builder()
                .name(cityDto.getName())
                .build();
    }

    @Override
    public void updateEntityFromDto(City city, CityDto cityDto) {
        city.setName(cityDto.getName());
    }
}
