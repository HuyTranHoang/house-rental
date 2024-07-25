package com.project.house.rental.controller;
import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PropertyDto> createProperty( @Valid @RequestBody PropertyDto propertyDto) {
        PropertyDto createdProperty = propertyService.create(propertyDto);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long id) {
        PropertyDto propertyDto = propertyService.getById(id);
        return new ResponseEntity<>(propertyDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
        public ResponseEntity<PropertyDto> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyDto propertyDto) {
            PropertyDto updatedProperty = propertyService.update(id,propertyDto);
            return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}