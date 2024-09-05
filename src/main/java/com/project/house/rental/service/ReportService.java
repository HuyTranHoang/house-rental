package com.project.house.rental.service;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Report;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<ReportDto> getAllReports(String username);

    ReportDto getReportById(long id);

    ReportDto createReport(ReportDto reportDto, HttpServletRequest request);

    void deleteReportById(long id);

    Map<String, Object> getAllReportsWithParams(ReportParams reportParams);

    void updateReportStatus(long reportId, String status);

}
