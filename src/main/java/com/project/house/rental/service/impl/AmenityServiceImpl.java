package com.project.house.rental.service.impl;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.entity.Amenity;
import com.project.house.rental.repository.AmenityRepository;
import com.project.house.rental.service.AmenityService;
import org.springframework.stereotype.Service;

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
}
