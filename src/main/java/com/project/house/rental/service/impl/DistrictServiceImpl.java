package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.entity.City;
import com.project.house.rental.entity.City_;
import com.project.house.rental.entity.District;
import com.project.house.rental.entity.District_;
import com.project.house.rental.repository.CityRepository;
import com.project.house.rental.repository.DistrictRepository;
import com.project.house.rental.service.DistrictService;
import com.project.house.rental.specification.DistrictSpecification;
import jakarta.persistence.NoResultException;
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
                .cityId(district.getCity().getId())
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

    @Override
    public Map<String, Object> getAllDistrictsWithParams(DistrictParams districtParams) {
        Specification<District> spec = Specification
                .where(DistrictSpecification.filterByCities(districtParams.getCityName()))
                .and(DistrictSpecification.searchByName(districtParams.getName()));

        if(!StringUtils.hasLength(districtParams.getSortBy())) {
            districtParams.setSortBy("createdAtDesc");
        }

        Sort sort = switch (districtParams.getSortBy()) {
            case "nameDesc" -> Sort.by(District_.NAME).descending();
            case "nameAsc" -> Sort.by(District_.NAME);
            case "createAtAsc" -> Sort.by(District_.CREATED_AT);
            case "cityNameAsc" -> Sort.by(District_.CITY + "." + City_.NAME);
            case "cityNameDesc" -> Sort.by(District_.CITY + "." + City_.NAME).descending();
            default -> Sort.by(District_.CREATED_AT).descending();
        };

        if(districtParams.getPageNumber() < 0){
            districtParams.setPageNumber(0);
        }

        if(districtParams.getPageSize() <= 0){
            districtParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                districtParams.getPageNumber(),
                districtParams.getPageSize(),
                sort
        );

        Page<District> districtPage = districtRepository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(
                districtPage.getTotalPages(),
                districtPage.getTotalElements(),
                districtPage.getNumber(),
                districtPage.getSize()
        );

        List<DistrictDto> districtDtoList = districtPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", districtDtoList
        );
    }

    public List<DistrictDto> getAllWithFilter(String city) {
        Specification<District> spec = DistrictSpecification.filterByCities(city);

        return getRepository().findAll(spec)
                .stream()
                .map(this::toDto)
                .toList();
    }

}