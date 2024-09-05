package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.City_;
import com.project.house.rental.exception.ConflictException;
import com.project.house.rental.mapper.CityMapper;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.service.CityService;
import com.project.house.rental.specification.CitySpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public CityServiceImpl(CityRepository cityRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.cityRepository = cityRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<CityDto> getAllCities() {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_CITY_FILTER);

        List<City> cityList = cityRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_CITY_FILTER);

        return cityList.stream()
                .map(CityMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public CityDto getCityById(long id) {

        City city = cityRepository.findByIdWithFilter(id);

        if (city == null) {
            throw new NoResultException("Không tìm thấy 'City' với id = " + id);
        }

        return CityMapper.INSTANCE.toDto(city);
    }

    @Override
    public CityDto createCity(CityDto cityDto) {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_CITY_FILTER);

        City existingCity = cityRepository.findByNameIgnoreCase(cityDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_CITY_FILTER);

        if (existingCity != null) {
            throw new ConflictException("Tên thành phố đã tồn tại");
        }

        City city = CityMapper.INSTANCE.toEntity(cityDto);
        city = cityRepository.save(city);
        return CityMapper.INSTANCE.toDto(city);
    }

    @Override
    public CityDto updateCity(long id, CityDto cityDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_CITY_FILTER);

        City city = cityRepository.findByIdWithFilter(id);

        if (city == null) {
            throw new NoResultException("Không tìm thấy thành phố với id = " + id);
        }

        City existingCity = cityRepository.findByNameIgnoreCase(cityDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_CITY_FILTER);

        if (existingCity != null && existingCity.getId() != id) {
            throw new ConflictException("Tên thành phố đã tồn tại");
        }

        CityMapper.INSTANCE.updateFromDto(cityDto, city);
        city = cityRepository.save(city);

        return CityMapper.INSTANCE.toDto(city);
    }

    @Override
    public void deleteCityById(long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy 'City' với id = " + id));

        cityRepository.deleteById(city.getId());
    }

    @Override
    public void deleteMultipleCities(List<Long> ids) {
        List<City> cityList = cityRepository.findAllById(ids);
        cityRepository.deleteAll(cityList);
    }

    @Override
    public Map<String, Object> getAllCitiesWithParams(CityParams cityParams) {
        Specification<City> spec = CitySpecification.searchByName(cityParams.getName());

        Sort sort = switch (cityParams.getSortBy()) {
            case "nameAsc" -> Sort.by(City_.NAME);
            case "nameDesc" -> Sort.by(City_.NAME).descending();
            case "createdAtAsc" -> Sort.by(City_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(City_.CREATED_AT).descending();
            default -> Sort.by(City_.ID).descending();
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

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_CITY_FILTER);

        Page<City> cityPage = cityRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_CITY_FILTER);

        PageInfo pageInfo = new PageInfo(cityPage);

        List<CityDto> cityDtoList = cityPage.stream()
                .map(CityMapper.INSTANCE::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", cityDtoList
        );
    }

}
