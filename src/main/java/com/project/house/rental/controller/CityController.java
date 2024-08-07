package com.project.house.rental.controller;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.service.CityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllCity(@ModelAttribute CityParams cityParams) {
        Map<String, Object> citiesWithPagination = cityService.getAllCitiesWithParams(cityParams);
        return ResponseEntity.ok(citiesWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDto>> getAllCitiesNoPaging() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable long id) {
        CityDto city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<CityDto> createCity(@RequestBody @Valid CityDto cityDto) {
        CityDto city = cityService.createCity(cityDto);
        return ResponseEntity.ok(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable long id, @RequestBody @Valid CityDto cityDto) {
        CityDto city = cityService.updateCity(id, cityDto);
        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable long id) {
        cityService.deleteCityById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleCities(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        cityService.deleteMultipleCities(ids);
        return ResponseEntity.noContent().build();
    }
}
