package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.City_;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.service.CityService;
import com.project.house.rental.specification.CitySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

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

    @Override
    public CityDto create(CityDto cityDto) {
        City existingCity = cityRepository.findByNameIgnoreCase(cityDto.getName());

        if (existingCity != null) {
            throw new IllegalArgumentException("City with name " + cityDto.getName() + " already exists");
        }

        return super.create(cityDto);
    }

    @Override
    public CityDto update(long id, CityDto cityDto) {
        City existingCity = cityRepository.findByNameIgnoreCase(cityDto.getName());

        if (existingCity != null && existingCity.getId() != id) {
            throw new IllegalArgumentException("City with name " + cityDto.getName() + " already exists");
        }

        return super.update(id, cityDto);
    }

    @Override
    public Map<String, Object> getAllCitiesWithParams(CityParams cityParams) {
        Specification<City> spec = CitySpecification.searchByName(cityParams.getName());

        if (!StringUtils.hasLength(cityParams.getSortBy())) {
            cityParams.setSortBy("createdAtDesc");
        }

        Sort sort = switch (cityParams.getSortBy()) {
            case "nameAsc" -> Sort.by(City_.NAME);
            case "nameDesc" -> Sort.by(City_.NAME).descending();
            case "createdAtAsc" -> Sort.by(City_.CREATED_AT);
            default -> Sort.by(City_.CREATED_AT).descending();
        };

        if (cityParams.getPageNumber() < 0) {
            cityParams.setPageNumber(0);
        }

        if (cityParams.getPageSize() <= 0) {
            cityParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                cityParams.getPageNumber(),
                cityParams.getPageSize(),
                sort
        );

        Page<City> cityPage = cityRepository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(
                cityPage.getNumber(),
                cityPage.getTotalElements(),
                cityPage.getTotalPages(),
                cityPage.getSize()
        );

        List<CityDto> cityDtoList = cityPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", cityDtoList
        );
    }


}
