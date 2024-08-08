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

import java.util.List;
import java.util.Map;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public AmenityServiceImpl(AmenityRepository amenitiesRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.amenityRepository = amenitiesRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<AmenityDto> getAllAmenities() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        List<Amenity> amenities = amenityRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        return amenities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public AmenityDto getAmenityById(long id) {
        Amenity amenity = amenityRepository.findByIdWithFilter(id);

        if (amenity == null) {
            throw new RuntimeException("Không tìm thấy tiện ích với id = " + id);
        }

        return toDto(amenity);
    }

    @Override
    public AmenityDto createAmenity(AmenityDto amenityDto) {
        Amenity existingAmenity = amenityRepository.findByNameIgnoreCase(amenityDto.getName());

        if (existingAmenity != null) {
            throw new RuntimeException("Tiện ích đã tồn tại");
        }

        Amenity amenities = toEntity(amenityDto);
        amenities = amenityRepository.save(amenities);
        return toDto(amenities);
    }

    @Override
    public AmenityDto updateAmenity(long id, AmenityDto amenityDto) {
        Amenity amenities = amenityRepository.findByIdWithFilter(id);

        if (amenities == null) {
            throw new RuntimeException("Không tìm thấy tiện ích với id = " + id);
        }

        Amenity existingAmenity = amenityRepository.findByNameIgnoreCase(amenityDto.getName());

        if (existingAmenity != null && existingAmenity.getId() != id) {
            throw new RuntimeException("Tiện ích đã tồn tại");
        }

        updateEntityFromDto(amenities, amenityDto);
        amenities = amenityRepository.save(amenities);

        return toDto(amenities);
    }

    @Override
    public void deleteAmenityById(long id) {
        Amenity amenities = amenityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiện ích với id = " + id));

        amenityRepository.deleteById(amenities.getId());
    }

    @Override
    public void deleteMultipleAmenities(List<Long> ids) {
        List<Amenity> amenityList = amenityRepository.findAllById(ids);
        amenityRepository.deleteAll(amenityList);
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

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        Page<Amenity> amenityPage = amenityRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_AMENITY_FILTER);

        PageInfo pageInfo = new PageInfo(amenityPage);

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
