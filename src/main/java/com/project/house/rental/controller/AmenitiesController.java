package com.project.house.rental.controller;

import com.project.house.rental.dto.AmenitiesDto;
import com.project.house.rental.service.AmenitiesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenitiesController {

    private final AmenitiesService amenitiesService;

    public AmenitiesController(AmenitiesService amenitiesService) {
        this.amenitiesService = amenitiesService;
    }
    @GetMapping({"/", ""})
    public ResponseEntity<List<AmenitiesDto>> getAllAmenities() {
        List<AmenitiesDto> amenities = amenitiesService.getAll();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenitiesDto> getAmenitiesById(@PathVariable long id) {
        AmenitiesDto amenities = amenitiesService.getById(id);
        return ResponseEntity.ok(amenities);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<AmenitiesDto> createAmenities(@RequestBody AmenitiesDto amenitiesDto) {
        AmenitiesDto amenities = amenitiesService.create(amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenitiesDto> updateAmenities(@PathVariable long id, @RequestBody AmenitiesDto amenitiesDto) {
        AmenitiesDto amenities = amenitiesService.update(id, amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenities(@PathVariable long id) {
        amenitiesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
