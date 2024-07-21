package com.project.house.rental.service;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.entity.District;

import java.util.List;
import java.util.Map;

public interface DistrictService extends GenericService<District, DistrictDto> {
    Map<String, Object> getAllDistrictsWithParams(DistrictParams districtParams);
    List<DistrictDto> getAllWithFilter(String filter);
}