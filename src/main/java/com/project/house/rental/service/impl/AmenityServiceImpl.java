package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.entity.Amenity;
import com.project.house.rental.entity.Amenity_;
import com.project.house.rental.repository.AmenityRepository;
import com.project.house.rental.service.AmenityService;
import com.project.house.rental.specification.AmenitySpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
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
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenitiesRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public AmenityServiceImpl(AmenityRepository amenitiesRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.amenitiesRepository = amenitiesRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<AmenityDto> getAllAmenities() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        List<Amenity> amenities = amenitiesRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        return amenities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public AmenityDto getAmenityById(long id) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        Amenity amenities = amenitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy 'Amenity' với id = " + id));

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        return toDto(amenities);
    }

    @Override
    public AmenityDto createAmenity(AmenityDto amenityDto) {
        Amenity amenities = toEntity(amenityDto);
        amenities = amenitiesRepository.save(amenities);
        return toDto(amenities);
    }

    @Override
    public AmenityDto updateAmenity(long id, AmenityDto amenityDto) {
        Amenity amenities = amenitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy 'Amenity' với id = " + id));

        updateEntityFromDto(amenities, amenityDto);
        amenities = amenitiesRepository.save(amenities);

        return toDto(amenities);
    }

    @Override
    public void deleteAmenityById(long id) {
        Amenity amenities = amenitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy 'Amenity' với id = " + id));

        amenitiesRepository.deleteById(amenities.getId());
    }

    @Override
    public Map<String, Object> getAllAmenitiesWithParams(AmenityParams amenityParams) {
        Specification<Amenity> spec = AmenitySpecification.searchByName(amenityParams.getName());

        Sort sort = switch (amenityParams.getSortBy()) {
            case "nameAsc" -> Sort.by(Amenity_.NAME);
            case "nameDesc" -> Sort.by(Amenity_.NAME).descending();
            case "createdAtAsc" -> Sort.by(Amenity_.CREATED_AT);
            default -> Sort.by(Amenity_.CREATED_AT).descending();
        };

        if (amenityParams.getPageNumber() < 0) {
            amenityParams.setPageNumber(0);
        }

        if (amenityParams.getPageSize() <= 0) {
            amenityParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                amenityParams.getPageNumber(),
                amenityParams.getPageSize(),
                sort
        );

        Page<Amenity> amenityPage = amenitiesRepository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(
                amenityPage.getNumber(),
                amenityPage.getTotalElements(),
                amenityPage.getTotalPages(),
                amenityPage.getSize()
        );

        List<AmenityDto> amenityDtoList = amenityPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", amenityDtoList
        );
    }

    @Override
    public AmenityDto toDto(Amenity amenities) {
        return AmenityDto.builder()
                .id(amenities.getId())
                .name(amenities.getName())
                .build();
    }

    @Override
    public Amenity toEntity(AmenityDto amenitiesDto) {
        return Amenity.builder()
                .name(amenitiesDto.getName())
                .build();
    }

    @Override
    public void updateEntityFromDto(Amenity amenities, AmenityDto amenitiesDto) {
        amenities.setName(amenitiesDto.getName());
    }
}
