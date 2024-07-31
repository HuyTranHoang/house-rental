package com.project.house.rental.controller;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.service.AmenityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenitiesService;

    public AmenityController(AmenityService amenitiesService) {
        this.amenitiesService = amenitiesService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllAmenitiesWithParams(@ModelAttribute AmenityParams amenityParams) {
        Map<String, Object> amenityWithParams = amenitiesService.getAllAmenitiesWithParams(amenityParams);
        return ResponseEntity.ok(amenityWithParams);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AmenityDto>> getAllAmenityNoPaging() {
        List<AmenityDto> amenity = amenitiesService.getAllAmenities();
        return ResponseEntity.ok(amenity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDto> getAmenitiesById(@PathVariable long id) {
        AmenityDto amenities = amenitiesService.getAmenityById(id);
        return ResponseEntity.ok(amenities);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<AmenityDto> createAmenities(@RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.createAmenity(amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenityDto> updateAmenities(@PathVariable long id, @RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.updateAmenity(id, amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenities(@PathVariable long id) {
        amenitiesService.deleteAmenityById(id);
        return ResponseEntity.noContent().build();
    }
}
