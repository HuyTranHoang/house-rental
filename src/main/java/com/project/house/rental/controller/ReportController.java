package com.project.house.rental.controller;

import com.project.house.rental.common.email.EmailSenderService;
import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReportDto>> getAllReports() {
        List<ReportDto> reportDtos = reportService.getAll();
        return ResponseEntity.ok(reportDtos);
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
