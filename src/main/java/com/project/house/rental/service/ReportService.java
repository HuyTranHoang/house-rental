package com.project.house.rental.service;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Report;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ReportService extends GenericService<Report, ReportDto> {
    Map<String, Object> getAllReportsWithParams(ReportParams reportParams);

    Report toEntity(ReportDto reportDto, HttpServletRequest request);

//    ReportDto createWithUserId(ReportDto reportDto, HttpServletRequest request);
}
