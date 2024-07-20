package com.project.house.rental.service.impl;

import com.project.house.rental.dto.AmenitiesDto;
import com.project.house.rental.entity.Amenities;
import com.project.house.rental.repository.AmenitiesRepository;
import com.project.house.rental.service.AmenitiesService;
import org.springframework.stereotype.Service;

@Service
public class AmenitiesServiceImpl extends GenericServiceImpl<Amenities, AmenitiesDto> implements AmenitiesService {

    private final AmenitiesRepository amenitiesRepository;

    public AmenitiesServiceImpl(AmenitiesRepository amenitiesRepository) {
        this.amenitiesRepository = amenitiesRepository;
    }


    @Override
    protected AmenitiesRepository getRepository() {
        return amenitiesRepository;
    }

    @Override
    public AmenitiesDto toDto(Amenities amenities) {
        return AmenitiesDto.builder()
                .id(amenities.getId())
                .name(amenities.getName())
                .build();
    }

    @Override
    public Amenities toEntity(AmenitiesDto amenitiesDto) {
        return Amenities.builder()
                .name(amenitiesDto.getName())
                .build();
    }

    @Override
    public void updateEntityFromDto(Amenities amenities, AmenitiesDto amenitiesDto) {
        amenities.setName(amenitiesDto.getName());
    }
}
