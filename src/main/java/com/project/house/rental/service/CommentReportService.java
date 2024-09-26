package com.project.house.rental.service;

import com.project.house.rental.dto.CommentReportDto;
import com.project.house.rental.dto.params.CommentReportParams;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface CommentReportService {

    List<CommentReportDto> getAllCommentReports(String username);

    CommentReportDto getCommentReportById(long id);

    CommentReportDto createCommentReport(CommentReportDto reportDto, HttpServletRequest request);

    void deleteCommentReportById(long id);

    Map<String, Object> getAllCommentReportsWithParams(CommentReportParams reportParams);

    void updateCommentReportStatus(long reportId, String status);

}
