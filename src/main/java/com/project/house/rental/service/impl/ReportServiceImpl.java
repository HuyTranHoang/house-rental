package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.Report_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.ReportRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.ReportService;
import com.project.house.rental.service.email.EmailSenderService;
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
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final PropertyRepository propertyRepository;
    private final EmailSenderService emailSenderService;
    private final HibernateFilterHelper hibernateFilterHelper;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, PropertyRepository propertyRepository, EmailSenderService emailSenderService, HibernateFilterHelper hibernateFilterHelper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.propertyRepository = propertyRepository;
        this.emailSenderService = emailSenderService;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    public List<ReportDto> getAllReports(String username) {
        Specification<Report> specification = ReportSpecification.filterByUsername(username);

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REPORT_FILTER);

        List<Report> reportList = reportRepository.findAll(specification);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REPORT_FILTER);

        return reportList.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ReportDto getReportById(long id) {
        Report report = reportRepository.findByIdWithFilter(id);

        if (report == null) {
            throw new NoResultException("Không tìm thấy report với id: " + id);
        }

        return toDto(report);
    }

    @Override
    public ReportDto createReport(ReportDto reportDto, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        reportDto.setUserId(currentUser.getId());

        Report newReport = toEntity(reportDto);

        emailSenderService.sendReportHTMLMail(currentUser.getEmail(), currentUser.getUsername(), newReport.getProperty().getTitle());

        return toDto(reportRepository.save(newReport));
    }

    @Override
    public void deleteReportById(long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy report với id: " + id));

        reportRepository.delete(report);
    }


    @Override
    public Map<String, Object> getAllReportsWithParams(ReportParams reportParams) {
        Specification<Report> specification = ReportSpecification.filterByUsername(reportParams.getUsername());

        Sort sort = switch (reportParams.getSortBy()) {
            case "createdAtAsc" -> Sort.by(Report_.CREATED_AT);
            default -> Sort.by(Report_.CREATED_AT).descending();
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

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REPORT_FILTER);

        Page<Report> reportPage = reportRepository.findAll(specification, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REPORT_FILTER);

        PageInfo pageInfo = new PageInfo(
                reportPage.getTotalPages(),
                reportPage.getTotalElements(),
                reportPage.getNumber(),
                reportPage.getSize()
        );

        List<ReportDto> reportDtoList = reportPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", reportDtoList
        );
    }


    @Override
    public void updateReportStatus(long reportId, String status) {

        Report report = reportRepository.findByIdWithFilter(reportId);

        if (report == null) {
            throw new NoResultException("Không tìm thấy report!");
        }

        Property property = propertyRepository.findByIdWithFilter(report.getProperty().getId());

        if (property == null) {
            throw new NoResultException("Không tìm thấy bài đăng!");
        }

        if (!isValidReportStatus(status)) {
            throw new IllegalArgumentException("Trạng thái [" + status + "] không hợp lệ");
        }

        report.setStatus(Report.ReportStatus.valueOf(status));

        if (status.equals("APPROVED")) {
            property.setBlocked(true);
            propertyRepository.save(property);
        }

        reportRepository.save(report);
    }

    private boolean isValidReportStatus(String status) {
        try {
            Report.ReportStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /*
        Helper method
     */

    @Override
    public ReportDto toDto(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .username(report.getUser().getUsername())
                .propertyId(report.getProperty().getId())
                .title(report.getProperty().getTitle())
                .reason(report.getReason())
                .build();
    }

    @Override
    public Report toEntity(ReportDto reportDto) {
        //TODO: check if user is deleted bằng hàm custom

        UserEntity currentUser = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + reportDto.getUserId()));

        Property currentProperty = propertyRepository.findByIdWithFilter(reportDto.getPropertyId());
        if (currentProperty == null) {
            throw new NoResultException("Không tìm thấy bài đăng!");
        }

        return Report.builder()
                .user(currentUser)
                .property(currentProperty)
                .reason(reportDto.getReason())
                .build();
    }
}
