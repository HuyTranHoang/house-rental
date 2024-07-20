package com.project.house.rental.service;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.entity.City;

import java.util.Map;

public interface CityService extends GenericService<City, CityDto> {
    Map<String, Object> getAllCitiesWithParams(CityParams cityParams);
}
