package com.project.house.rental.controller;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.service.AmenityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenitiesService;

    public AmenityController(AmenityService amenitiesService) {
        this.amenitiesService = amenitiesService;
    }
    @GetMapping({"/", ""})
    public ResponseEntity<List<AmenityDto>> getAllAmenities() {
        List<AmenityDto> amenities = amenitiesService.getAll();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDto> getAmenitiesById(@PathVariable long id) {
        AmenityDto amenities = amenitiesService.getById(id);
        return ResponseEntity.ok(amenities);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<AmenityDto> createAmenities(@RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.create(amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenityDto> updateAmenities(@PathVariable long id, @RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.update(id, amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenities(@PathVariable long id) {
        amenitiesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
