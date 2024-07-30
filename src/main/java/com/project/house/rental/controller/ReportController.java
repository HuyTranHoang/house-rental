package com.project.house.rental.controller;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllReports(@ModelAttribute ReportParams reportParams) {
        Map<String, Object> reportsWithPagination = reportService.getAllReportsWithParams(reportParams);
        return ResponseEntity.ok(reportsWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReportDto>> getAllReports(@RequestParam(required = false) String username) {
        List<ReportDto> reportDtos = reportService.getAllWithFilter(username);
        return ResponseEntity.ok(reportDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getDistrictById(@PathVariable long id){
        ReportDto reportDto = reportService.getById(id);
        return ResponseEntity.ok(reportDto);
    }

    @PostMapping
    public ResponseEntity<ReportDto> createReport(@Valid @RequestBody ReportDto reportDto, HttpServletRequest request) {
        ReportDto report = reportService.createReport(reportDto, request);
        return  ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
