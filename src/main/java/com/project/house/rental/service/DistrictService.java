package com.project.house.rental.service;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.entity.District;

import java.util.List;
import java.util.Map;

public interface DistrictService {

    List<DistrictDto> getAllDistricts(long cityId);

    DistrictDto getDistrictById(long id);

    DistrictDto createDistrict(DistrictDto districtDto);

    DistrictDto updateDistrict(long id, DistrictDto districtDto);

    void deleteDistrictById(long id);

    void deleteDistricts(List<Long> ids);

    Map<String, Object> getAllDistrictsWithParams(DistrictParams districtParams);

}