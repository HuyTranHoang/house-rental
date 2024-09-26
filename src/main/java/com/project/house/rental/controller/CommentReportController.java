package com.project.house.rental.controller;

import com.project.house.rental.dto.CommentReportDto;
import com.project.house.rental.dto.params.CommentReportParams;
import com.project.house.rental.service.CommentReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class CommentReportController {
    private final CommentReportService commentReportService;

    public CommentReportController(CommentReportService commentReportService) {
        this.commentReportService = commentReportService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllReports(@ModelAttribute CommentReportParams reportParams) {
        Map<String, Object> reportsWithPagination = commentReportService.getAllCommentReportsWithParams(reportParams);
        return ResponseEntity.ok(reportsWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentReportDto>> getAllReports(@RequestParam(required = false) String username) {
        List<CommentReportDto> reportDtos = commentReportService.getAllCommentReports(username);
        return ResponseEntity.ok(reportDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentReportDto> getDistrictById(@PathVariable long id){
        CommentReportDto reportDto = commentReportService.getCommentReportById(id);
        return ResponseEntity.ok(reportDto);
    }

    @PostMapping
    public ResponseEntity<CommentReportDto> createReport(@Valid @RequestBody CommentReportDto reportDto, HttpServletRequest request) {
        CommentReportDto report = commentReportService.createCommentReport(reportDto, request);
        return  ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        commentReportService.deleteCommentReportById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReportStatus(@PathVariable Long id,@RequestParam String status) {
        commentReportService.updateCommentReportStatus(id, status);
        return ResponseEntity.noContent().build();
    }

}
