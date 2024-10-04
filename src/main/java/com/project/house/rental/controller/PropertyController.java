package com.project.house.rental.controller;

import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import com.project.house.rental.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;


    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllPropertiesWithParams(@ModelAttribute PropertyParams propertyParams) {
        Map<String, Object> propertyWithParams = propertyService.getAllPropertiesWithParams(propertyParams);
        return ResponseEntity.ok(propertyWithParams);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@Valid @ModelAttribute PropertyDto propertyDto, @RequestParam MultipartFile[] images, @RequestParam MultipartFile thumbnailImage) throws IOException {
        PropertyDto createdProperty = propertyService.createProperty(propertyDto, images, thumbnailImage);
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

    @DeleteMapping("/self/{id}")
    public ResponseEntity<Void> selfDeleteProperty(@PathVariable Long id, HttpServletRequest request) {
        propertyService.selfDeletePropertyById(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<PropertyDto> blockProperty(@PathVariable Long id, @RequestParam String status) {
        PropertyDto propertyDto = propertyService.blockProperty(id, status);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<PropertyDto> updatePropertyStatus(@PathVariable Long id, @RequestParam String status) {
        PropertyDto propertyDto = propertyService.updatePropertyStatus(id, status);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<PropertyDto> hideProperty(@PathVariable Long id, HttpServletRequest request) {
        PropertyDto propertyDto = propertyService.hideProperty(id, request);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/refresh/{id}")
    public ResponseEntity<PropertyDto> refreshProperty(@PathVariable Long id, HttpServletRequest request) {
        PropertyDto propertyDto = propertyService.refreshProperty(id, request);
        return ResponseEntity.ok(propertyDto);
    }

    @PutMapping("/priority/{id}")
    public ResponseEntity<PropertyDto> prioritizeProperty(@PathVariable Long id, HttpServletRequest request) {
        PropertyDto propertyDto = propertyService.prioritizeProperty(id, request);
        return ResponseEntity.ok(propertyDto);
    }

    @GetMapping("/priority")
    public ResponseEntity<List<PropertyDto>> getPriorityProperties() {
        List<PropertyDto> properties = propertyService.getPriorityProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/related/{propertyId}")
    public ResponseEntity<List<PropertyDto>> getRelatedProperties(@PathVariable Long propertyId) {
        List<PropertyDto> properties = propertyService.getRelatedProperties(propertyId);
        return ResponseEntity.ok(properties);
    }

}