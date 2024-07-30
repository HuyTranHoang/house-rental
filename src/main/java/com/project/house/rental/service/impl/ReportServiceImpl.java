package com.project.house.rental.service.impl;

import com.project.house.rental.common.email.EmailSenderService;
import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.params.ReportParams;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.GenericRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.ReportRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.ReportService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    protected GenericRepository<Report> getRepository() {
        return reportRepository;
    }

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
    public ReportDto createReport(ReportDto reportDto, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản!");
        }

        Property currentProperty = propertyRepository.findById(reportDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng này!"));

        reportDto.setUserId(currentUser.getId());
        reportDto.setUsername(currentUser.getUsername());
        reportDto.setPropertyId(currentProperty.getId());
        reportDto.setTitle(currentProperty.getTitle());

        Report newReport = toEntity(reportDto);

        emailSenderService.sendEmail(currentUser.getEmail(), "Thông báo Report", reportDto.getReason());

        return toDto(reportRepository.save(newReport));
    }

    @Override
    public void updateEntityFromDto(Report report, ReportDto reportDto) {
    //Ko update
    }

    @Override
    public Map<String, Object> getAllReportsWithParams(ReportParams reportParams) {
        return null;
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return jwtTokenProvider.getSubject(token);
        }
        return null;
    }
}
