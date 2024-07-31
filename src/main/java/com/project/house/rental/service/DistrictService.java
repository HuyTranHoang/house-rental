package com.project.house.rental.service;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.entity.District;

import java.util.List;
import java.util.Map;

public interface DistrictService {

    List<DistrictDto> getAllDistricts(String cityName);

    DistrictDto getDistrictById(long id);

    DistrictDto createDistrict(DistrictDto districtDto);

    DistrictDto updateDistrict(long id, DistrictDto districtDto);

    void deleteDistrictById(long id);

    Map<String, Object> getAllDistrictsWithParams(DistrictParams districtParams);

    DistrictDto toDto(District district);

    District toEntity(DistrictDto districtDto);

    void updateEntityFromDto(District district, DistrictDto districtDto);
}