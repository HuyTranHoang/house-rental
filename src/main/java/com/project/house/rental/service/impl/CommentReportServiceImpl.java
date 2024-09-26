package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.CommentReportDto;
import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.CommentReportParams;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.*;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserEntity_;
import com.project.house.rental.mapper.CommentReportMapper;
import com.project.house.rental.mapper.ReportMapper;
import com.project.house.rental.repository.CommentReportRepository;
import com.project.house.rental.repository.CommentRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.ReportRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.CommentReportService;
import com.project.house.rental.service.ReportService;
import com.project.house.rental.service.email.EmailSenderService;
import com.project.house.rental.specification.CommentReportSpecification;
import com.project.house.rental.specification.ReportSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CommentReportServiceImpl implements CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final PropertyRepository propertyRepository;
    private final EmailSenderService emailSenderService;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final CommentReportMapper commentReportMapper;
    private final CommentRepository commentRepository;

    public CommentReportServiceImpl(CommentReportRepository commentReportRepository, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, PropertyRepository propertyRepository, EmailSenderService emailSenderService, HibernateFilterHelper hibernateFilterHelper, CommentReportMapper commentReportMapper, CommentRepository commentRepository) {
        this.commentReportRepository = commentReportRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.propertyRepository = propertyRepository;
        this.emailSenderService = emailSenderService;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.commentReportMapper = commentReportMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentReportDto> getAllCommentReports(String username) {
        Specification<CommentReport> specification = CommentReportSpecification.filterByUsername(username);

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_COMMENT_REPORT_FILTER);

        List<CommentReport> reportList = commentReportRepository.findAll(specification);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_COMMENT_REPORT_FILTER);

        return reportList.stream()
                .map(commentReportMapper::toDto)
                .toList();
    }

    @Override
    public CommentReportDto getCommentReportById(long id) {
        CommentReport commentReport = commentReportRepository.findByIdWithFilter(id);

        if (commentReport == null) {
            throw new NoResultException("Không tìm thấy report với id: " + id);
        }

        return commentReportMapper.toDto(commentReport);
    }

    @Override
    public CommentReportDto createCommentReport(CommentReportDto reportDto, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        reportDto.setUserId(currentUser.getId());

        CommentReport newReport = commentReportMapper.toEntity(reportDto);

        newReport.setStatus(CommentReport.ReportStatus.PENDING);

        // TODO: Bật lên khi test demo, cần viết lại email
//         emailSenderService.sendReportHTMLMail(currentUser.getEmail(), currentUser.getUsername(), newReport.getProperty().getTitle());

        return commentReportMapper.toDto(commentReportRepository.save(newReport));
    }

    @Override
    public void deleteCommentReportById(long id) {
        CommentReport report = commentReportRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy report với id: " + id));

        commentReportRepository.delete(report);
    }

    @Override
    public Map<String, Object> getAllCommentReportsWithParams(CommentReportParams reportParams) {
        Specification<CommentReport> specification = CommentReportSpecification.filterByUsername(reportParams.getUsername())
                .and(CommentReportSpecification.filterByStatus(reportParams.getStatus()))
                .and(CommentReportSpecification.filterByCategory(reportParams.getCategory()));

        Sort sort = switch (reportParams.getSortBy()) {
            case "usernameAsc" -> Sort.by(CommentReport_.USER + "." + UserEntity_.USERNAME);
            case "usernameDesc" -> Sort.by(CommentReport_.USER + "." + UserEntity_.USERNAME).descending();
            case "categoryAsc" -> Sort.by(CommentReport_.CATEGORY);
            case "categoryDesc" -> Sort.by(CommentReport_.CATEGORY).descending();
            case "createdAtAsc" -> Sort.by(CommentReport_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(CommentReport_.CREATED_AT).descending();
            default -> Sort.by(CommentReport_.ID).descending();
        };

        if (reportParams.getPageNumber() < 0) {
            reportParams.setPageNumber(0);
        }

        if (reportParams.getPageSize() <= 0) {
            reportParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                reportParams.getPageNumber(),
                reportParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_COMMENT_REPORT_FILTER);

        Page<CommentReport> reportPage = commentReportRepository.findAll(specification, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_COMMENT_REPORT_FILTER);

        PageInfo pageInfo = new PageInfo(reportPage);

        List<CommentReportDto> reportDtoList = reportPage.stream()
                .map(commentReportMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", reportDtoList
        );
    }

    @Override
    public void updateCommentReportStatus(long reportId, String status) {

        CommentReport report = commentReportRepository.findByIdWithFilter(reportId);

        if (report == null) {
            throw new NoResultException("Không tìm thấy report!");
        }

        Comment comment = commentRepository.findByIdWithFilter(report.getComment().getId());

        if (comment == null) {
            throw new NoResultException("Không tìm thấy bình luận!");
        }

        if (!isValidReportStatus(status)) {
            throw new IllegalArgumentException("Trạng thái [" + status + "] không hợp lệ");
        }

        report.setStatus(CommentReport.ReportStatus.valueOf(status));

        if (status.equals("APPROVED")) {

            comment.setBlocked(true);
            commentRepository.save(comment);

            List<CommentReport> pendingReports = commentReportRepository.findAllByCommentIdAndStatus(comment.getId(), CommentReport.ReportStatus.PENDING);

            for (CommentReport pendingReport : pendingReports) {
                pendingReport.setStatus(CommentReport.ReportStatus.APPROVED);
                commentReportRepository.save(pendingReport);
            }

            //TODO: Bật lên khi test demo
            //emailSenderService.sendBlockHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
        }

        commentReportRepository.save(report);
    }

    private boolean isValidReportStatus(String status) {
        try {
            CommentReport.ReportStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
