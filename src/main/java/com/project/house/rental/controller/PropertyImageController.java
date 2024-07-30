package com.project.house.rental.controller;

import com.project.house.rental.dto.PropertyImageDto;
import com.project.house.rental.service.PropertyImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/property-images")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(PropertyImageService propertyImageService) {
        this.propertyImageService = propertyImageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<PropertyImageDto>> getPropertyImageByPropertyId(@PathVariable long id) {
        List<PropertyImageDto> propertyImageDtos = propertyImageService.findByPropertyId(id);
        return ResponseEntity.ok(propertyImageDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropertyImageByPropertyId(@PathVariable long id) throws IOException {
        propertyImageService.deleteByPropertyId(id);
        return ResponseEntity.noContent().build();
    }
}
