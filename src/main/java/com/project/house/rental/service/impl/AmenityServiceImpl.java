package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.entity.Amenity;
import com.project.house.rental.entity.Amenity_;
import com.project.house.rental.repository.AmenityRepository;
import com.project.house.rental.service.AmenityService;
import com.project.house.rental.specification.AmenitySpecification;
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
public class AmenityServiceImpl extends GenericServiceImpl<Amenity, AmenityDto> implements AmenityService {

    private final AmenityRepository amenitiesRepository;

    public AmenityServiceImpl(AmenityRepository amenitiesRepository) {
        this.amenitiesRepository = amenitiesRepository;
    }

    @Override
    protected AmenityRepository getRepository() {
        return amenitiesRepository;
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
    @Override
    public Map<String, Object> getAllAmenitiesWithParams(AmenityParams amenityParams) {
        Specification<Amenity> spec = AmenitySpecification.searchByName(amenityParams.getName());

        if (!StringUtils.hasLength(amenityParams.getSortBy())) {
            amenityParams.setSortBy("createdAtDesc");
        }

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
}
