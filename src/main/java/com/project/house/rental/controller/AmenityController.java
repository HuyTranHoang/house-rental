package com.project.house.rental.controller;

import com.project.house.rental.dto.AmenityDto;
import com.project.house.rental.dto.params.AmenityParams;
import com.project.house.rental.service.AmenityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amenity")
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
    public ResponseEntity<List<AmenityDto>> getAllAmenitiesNoPaging() {
        List<AmenityDto> amenity = amenitiesService.getAllAmenities();
        return ResponseEntity.ok(amenity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDto> getAmenityById(@PathVariable long id) {
        AmenityDto amenities = amenitiesService.getAmenityById(id);
        return ResponseEntity.ok(amenities);
    }

    @PreAuthorize("hasAnyAuthority('amenity:create', 'admin:all')")
    @PostMapping({"/", ""})
    public ResponseEntity<AmenityDto> createAmenity(@RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.createAmenity(amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @PreAuthorize("hasAnyAuthority('amenity:update', 'admin:all')")
    @PutMapping("/{id}")
    public ResponseEntity<AmenityDto> updateAmenity(@PathVariable long id, @RequestBody @Valid AmenityDto amenitiesDto) {
        AmenityDto amenities = amenitiesService.updateAmenity(id, amenitiesDto);
        return ResponseEntity.ok(amenities);
    }

    @PreAuthorize("hasAnyAuthority('amenity:delete', 'admin:all')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable long id) {
        amenitiesService.deleteAmenityById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('amenity:delete', 'admin:all')")
    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleAmenity(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        amenitiesService.deleteMultipleAmenities(ids);
        return ResponseEntity.noContent().build();
    }
}
