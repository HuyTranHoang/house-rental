package com.project.house.rental.controller;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;


    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PropertyDto>> getAllProperty() {
        List<PropertyDto> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@Valid @ModelAttribute PropertyDto propertyDto, @RequestParam MultipartFile[] images) throws IOException {
        PropertyDto createdProperty = propertyService.createProperty(propertyDto, images);
        return ResponseEntity.ok(createdProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long id) {
        PropertyDto propertyDto = propertyService.getPropertyById(id);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDto> updateProperty(@PathVariable Long id, @Valid @ModelAttribute PropertyDto propertyDto, @RequestParam(required = false) MultipartFile[] images) throws IOException {
        PropertyDto updatedProperty = propertyService.updateProperty(id, propertyDto, images);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deletePropertyById(id);
        return ResponseEntity.noContent().build();
    }

}