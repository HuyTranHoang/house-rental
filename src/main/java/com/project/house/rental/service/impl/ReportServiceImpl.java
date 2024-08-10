package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.Report_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserEntity_;
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
import java.util.Optional;


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

        newReport.setStatus(Report.ReportStatus.PENDING);

        // TODO: Bật lên khi test demo
//         emailSenderService.sendReportHTMLMail(currentUser.getEmail(), currentUser.getUsername(), newReport.getProperty().getTitle());

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
        Specification<Report> specification = ReportSpecification.filterByUsername(reportParams.getUsername())
                .and(ReportSpecification.filterByStatus(reportParams.getStatus()))
                .and(ReportSpecification.filterByCategory(reportParams.getCategory()));

        Sort sort = switch (reportParams.getSortBy()) {
            case "usernameAsc" -> Sort.by(Report_.USER + "." + UserEntity_.USERNAME);
            case "usernameDesc" -> Sort.by(Report_.USER + "." + UserEntity_.USERNAME).descending();
            case "titleAsc" -> Sort.by(Report_.PROPERTY + "." + Property_.TITLE);
            case "titleDesc" -> Sort.by(Report_.PROPERTY + "." + Property_.TITLE).descending();
            case "categoryAsc" -> Sort.by(Report_.CATEGORY);
            case "categoryDesc" -> Sort.by(Report_.CATEGORY).descending();
            case "createdAtAsc" -> Sort.by(Report_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Report_.CREATED_AT).descending();
            default -> Sort.by(Report_.ID).descending();
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

        PageInfo pageInfo = new PageInfo(reportPage);

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

            List<Report> pendingReports = reportRepository.findAllByPropertyIdAndStatus(property.getId(), Report.ReportStatus.PENDING);

            for (Report pendingReport : pendingReports) {
                pendingReport.setStatus(Report.ReportStatus.APPROVED);
                reportRepository.save(pendingReport);
            }

            //TODO: Cần gửi email thông báo cho người dùng đăng bài viết
            emailSenderService.sendReportHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
        }

        reportRepository.save(report);
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
                .status(String.valueOf(report.getStatus()))
                .category(String.valueOf(report.getCategory()))
                .createdAt(report.getCreatedAt())
                .build();
    }

    @Override
    public Report toEntity(ReportDto reportDto) {
        UserEntity currentUser = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + reportDto.getUserId()));

        Property currentProperty = propertyRepository.findByIdWithFilter(reportDto.getPropertyId());
        if (currentProperty == null) {
            throw new NoResultException("Không tìm thấy bài đăng!");
        }

        if (!isValidReportCategory(reportDto.getCategory())) {
            throw new IllegalArgumentException("Danh mục [" + reportDto.getCategory() + "] không hợp lệ");
        }

        return Report.builder()
                .user(currentUser)
                .property(currentProperty)
                .reason(reportDto.getReason())
                .category(Report.ReportCategory.valueOf(reportDto.getCategory()))
                .build();
    }

    private boolean isValidReportStatus(String status) {
        try {
            Report.ReportStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidReportCategory(String cateogry) {
        try {
            Report.ReportCategory.valueOf(cateogry);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
