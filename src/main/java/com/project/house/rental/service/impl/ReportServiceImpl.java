package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
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
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service
public class ReportServiceImpl extends GenericServiceImpl<Report, ReportDto> implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final PropertyRepository propertyRepository;
    private final EmailSenderService emailSenderService;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, PropertyRepository propertyRepository, EmailSenderService emailSenderService) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.propertyRepository = propertyRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    protected ReportRepository getRepository() {
        return reportRepository;
    }

    @Override
    public ReportDto create(ReportDto reportDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ReportDto create(ReportDto reportDto, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản!");
        }

        reportDto.setUserId(currentUser.getId());

        Report newReport = toEntity(reportDto);

        emailSenderService.sendReportHTMLMail(currentUser.getEmail(), currentUser.getUsername(), newReport.getProperty().getTitle());

        return toDto(reportRepository.save(newReport));
    }

    @Override
    public Map<String, Object> getAllReportsWithParams(ReportParams reportParams) {
        Specification<Report> specification = ReportSpecification.filterByUsername(reportParams.getUsername());

        if (!StringUtils.hasLength(reportParams.getSortBy())) {
            reportParams.setSortBy("createdAtDesc");
        }

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

        Page<Report> reportPage = reportRepository.findAll(specification, pageable);

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
        UserEntity currentUser = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user này!"));

        Property currentProperty = propertyRepository.findById(reportDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng này!"));

        return Report.builder()
                .user(currentUser)
                .property(currentProperty)
                .reason(reportDto.getReason())
                .build();
    }

    @Override
    public void updateEntityFromDto(Report report, ReportDto reportDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return jwtTokenProvider.getSubject(token);
        }
        return null;
    }

    public List<ReportDto> getAllWithFilter(String username) {
        Specification<Report> specification = ReportSpecification.filterByUsername(username);

        return getRepository().findAll(specification)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void updateReportStatus(long reportId, String status) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NoResultException("Không tìm thấy report!"));

        Property property = propertyRepository.findById(report.getProperty().getId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng!"));

        if (!isValidReportStatus(status)) {
            throw new IllegalArgumentException("Trạng thái [" + status + "] không hợp lệ");
        }

        report.setStatus(Report.ReportStatus.valueOf(status));

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
}
