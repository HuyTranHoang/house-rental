package com.project.house.rental.controller;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> cities = cityService.getAll();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable long id) {
        CityDto city = cityService.getById(id);
        return ResponseEntity.ok(city);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        CityDto city = cityService.create(cityDto);
        return ResponseEntity.ok(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable long id, @RequestBody CityDto cityDto) {
        CityDto city = cityService.update(id, cityDto);
        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable long id) {
        cityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
