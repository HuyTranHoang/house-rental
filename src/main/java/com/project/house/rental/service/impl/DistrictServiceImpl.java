package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
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
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public DistrictServiceImpl(DistrictRepository districtRepository, CityRepository cityRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }


    @Override
    public List<DistrictDto> getAllDistricts(long cityId) {

        Specification<District> spec = DistrictSpecification.filterByCity(cityId);

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_DISTRICT_FILTER);

        List<District> districts = districtRepository.findAll(spec);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_DISTRICT_FILTER);

        return districts.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public DistrictDto getDistrictById(long id) {

        District district = districtRepository.findByIdWithFilter(id);

        if (district == null) {
            throw new NoResultException("Không tìm thấy quận với id: " + id);
        }

        return toDto(district);
    }

    @Override
    public DistrictDto createDistrict(DistrictDto districtDto) {
        District district = toEntity(districtDto);

        district = districtRepository.save(district);

        return toDto(district);
    }

    @Override
    public DistrictDto updateDistrict(long id, DistrictDto districtDto) {
        District district = districtRepository.findByIdWithFilter(id);

        if (district == null) {
            throw new NoResultException("Không tìm thấy quận với id: " + id);
        }

        updateEntityFromDto(district, districtDto);

        district = districtRepository.save(district);

        return toDto(district);
    }

    @Override
    public void deleteDistrictById(long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy quận với id: " + id));

        districtRepository.deleteById(district.getId());
    }

    public Map<String, Object> getAllDistrictsWithParams(DistrictParams districtParams) {
        Specification<District> spec = DistrictSpecification.searchByName(districtParams.getName())
                .and(DistrictSpecification.filterByCity(districtParams.getCityId()));

        Sort sort = switch (districtParams.getSortBy()) {
            case "nameDesc" -> Sort.by(District_.NAME).descending();
            case "nameAsc" -> Sort.by(District_.NAME);
            case "createAtAsc" -> Sort.by(District_.CREATED_AT);
            case "cityNameAsc" -> Sort.by(District_.CITY + "." + City_.NAME);
            case "cityNameDesc" -> Sort.by(District_.CITY + "." + City_.NAME).descending();
            default -> Sort.by(District_.CREATED_AT).descending();
        };

        if (districtParams.getPageNumber() < 0) {
            districtParams.setPageNumber(0);
        }

        if (districtParams.getPageSize() <= 0) {
            districtParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                districtParams.getPageNumber(),
                districtParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_DISTRICT_FILTER);

        Page<District> districtPage = districtRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_DISTRICT_FILTER);

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
                .orElseThrow(() -> new NoResultException("Không tìm thấy 'City' với id: " + districtDto.getCityId()));

        return District.builder()
                .name(districtDto.getName())
                .city(city)
                .build();
    }

    @Override
    public void updateEntityFromDto(District district, DistrictDto districtDto) {
        City city = cityRepository.findById(districtDto.getCityId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy 'City' với id: " + districtDto.getCityId()));

        district.setName(districtDto.getName());
        district.setCity(city);
    }
}