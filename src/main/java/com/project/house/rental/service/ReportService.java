package com.project.house.rental.service;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Report;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface ReportService extends GenericService<Report, ReportDto> {
    Map<String, Object> getAllReportsWithParams(ReportParams reportParams);
    ReportDto create(ReportDto reportDto, HttpServletRequest request);
    List<ReportDto> getAllWithFilter(String filter);

    void updateReportStatus(long reportId, String status);
}
