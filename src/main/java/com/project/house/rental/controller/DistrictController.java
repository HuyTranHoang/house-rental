package com.project.house.rental.controller;


import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.service.DistrictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/district")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String,Object>> getAllDistrict(@ModelAttribute DistrictParams districtParams) {
        Map<String, Object> districtsWithPagination = districtService.getAllDistrictsWithParams(districtParams);
        return ResponseEntity.ok(districtsWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DistrictDto>> getAllDistrictsNoPaging(@RequestParam(required = false) String city) {
        List<DistrictDto> districts = districtService.getAllWithFilter(city);
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable long id){
        DistrictDto district = districtService.getById(id);
        return ResponseEntity.ok(district);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistrictDto> updateDistrict(@PathVariable long id, @RequestBody DistrictDto districtDto){
        DistrictDto district = districtService.update(id, districtDto);
        return ResponseEntity.ok(district);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable long id){
        districtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}