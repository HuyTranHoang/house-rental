package com.project.house.rental.service.impl;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.entity.District;
import com.project.house.rental.repository.DistrictsRepository;
import com.project.house.rental.service.DistrictService;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, DistrictDto> implements DistrictService {
    private final DistrictsRepository  districtsRepository;

    public DistrictServiceImpl(DistrictsRepository districtsRepository) {
        this.districtsRepository = districtsRepository;
    }

    @Override
    protected DistrictsRepository getRepository() {
        return districtsRepository;
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
        return District.builder()
                .name(districtDto.getName())
                .build();
    }

    @Override
    public void updateEntityFromDto(District district, DistrictDto districtDto) {
        district.setName(districtDto.getName());
    }
}
