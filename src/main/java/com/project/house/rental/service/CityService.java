package com.project.house.rental.service;


import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.params.CityParams;

import java.util.List;
import java.util.Map;

public interface CityService {
    List<CityDto> getAllCities();

    CityDto getCityById(long id);

    CityDto createCity(CityDto cityDto);

    CityDto updateCity(long id, CityDto cityDto);

    void deleteCityById(long id);

    void deleteMultipleCities(List<Long> ids);

    Map<String, Object> getAllCitiesWithParams(CityParams cityParams);
}
