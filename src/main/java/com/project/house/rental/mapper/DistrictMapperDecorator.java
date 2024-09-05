package com.project.house.rental.mapper;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.District;
import com.project.house.rental.repository.CityRepository;
import jakarta.persistence.NoResultException;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class DistrictMapperDecorator implements DistrictMapper {

    @Autowired
    @Qualifier("delegate")
    private DistrictMapper delegate;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public District toEntity(DistrictDto districtDto) {
        District district = delegate.toEntity(districtDto);

        City city = cityRepository.findById(districtDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy thành phố với id: " + districtDto.getCityId()));

        district.setCity(city);
        return district;
    }

    @Override
    public void updateEntityFromDto(DistrictDto districtDto, @MappingTarget District district) {
        delegate.updateEntityFromDto(districtDto, district);

        City city = cityRepository.findById(districtDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy thành phố với id: " + districtDto.getCityId()));

        district.setCity(city);
    }

}
