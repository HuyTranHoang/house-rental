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
        List<PropertyDto> properties = propertyService.getAll();
        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@Valid @ModelAttribute PropertyDto propertyDto, @RequestParam MultipartFile[] images) throws IOException {
        PropertyDto createdProperty = propertyService.create(propertyDto, images);
        return ResponseEntity.ok(createdProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long id) {
        PropertyDto propertyDto = propertyService.getById(id);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDto> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyDto propertyDto) {
        PropertyDto updatedProperty = propertyService.update(id, propertyDto);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}