package com.project.house.rental.service.impl;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.District;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.repository.DistrictRepository;
import com.project.house.rental.service.DistrictService;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;


@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, DistrictDto> implements DistrictService {
    private final DistrictRepository  districtRepository;
    private final CityRepository cityRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository, CityRepository cityRepository) {
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    protected DistrictRepository getRepository() {
        return districtRepository;
    }

    @Override
    public DistrictDto toDto(District district) {
        return DistrictDto.builder()
                .id(district.getId())
                .name(district.getName())
                .cityName(district.getCity().getName())
                .build();
    }

    @Override
    public District toEntity(DistrictDto districtDto) {
        City city = cityRepository.findById(districtDto.getCityId())
                .orElseThrow(() -> new NoResultException("Not found City with id: " + districtDto.getCityId()));

        return District.builder()
                .name(districtDto.getName())
                .city(city)
                .build();
    }

    @Override
    public void updateEntityFromDto(District district, DistrictDto districtDto) {
        City city = cityRepository.findById(districtDto.getCityId())
                .orElseThrow(() -> new NoResultException("Not found City with id: " + districtDto.getCityId()));

        district.setId(districtDto.getId());
        district.setName(districtDto.getName());
        district.setCity(city);
    }
}