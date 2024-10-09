package com.project.house.rental.controller;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.service.DistrictService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Map<String, Object>> getAllDistrict(@ModelAttribute DistrictParams districtParams) {
        Map<String, Object> districtsWithPagination = districtService.getAllDistrictsWithParams(districtParams);
        return ResponseEntity.ok(districtsWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DistrictDto>> getAllDistrictsNoPaging(@RequestParam(required = false, defaultValue = "0") long cityId) {
        List<DistrictDto> districts = districtService.getAllDistricts(cityId);
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable long id) {
        DistrictDto district = districtService.getDistrictById(id);
        return ResponseEntity.ok(district);
    }

    @PreAuthorize("hasAnyAuthority('district:create', 'admin:all')")
    @PostMapping({"/", ""})
    public ResponseEntity<DistrictDto> createDistrict(@RequestBody @Valid DistrictDto districtDto) {
        DistrictDto district = districtService.createDistrict(districtDto);
        return ResponseEntity.ok(district);
    }

    @PreAuthorize("hasAnyAuthority('district:update', 'admin:all')")
    @PutMapping("/{id}")
    public ResponseEntity<DistrictDto> updateDistrict(@PathVariable long id, @RequestBody @Valid DistrictDto districtDto) {
        DistrictDto district = districtService.updateDistrict(id, districtDto);
        return ResponseEntity.ok(district);
    }

    @PreAuthorize("hasAnyAuthority('district:delete', 'admin:all')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable long id) {
        districtService.deleteDistrictById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('district:delete', 'admin:all')")
    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleDistricts(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        districtService.deleteDistricts(ids);
        return ResponseEntity.noContent().build();
    }
}