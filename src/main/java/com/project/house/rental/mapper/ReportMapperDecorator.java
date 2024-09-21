package com.project.house.rental.mapper;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ReportMapperDecorator implements ReportMapper {

    @Autowired
    @Qualifier("delegate")
    private ReportMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Report toEntity(ReportDto reportDto) {
        Report report = delegate.toEntity(reportDto);

        UserEntity currentUser = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + reportDto.getUserId()));

        Property currentProperty = propertyRepository.findByIdWithFilter(reportDto.getPropertyId());
        if (currentProperty == null) {
            throw new NoResultException("Không tìm thấy bài đăng!");
        }

        if (!isValidReportCategory(reportDto.getCategory())) {
            throw new IllegalArgumentException("Danh mục [" + reportDto.getCategory() + "] không hợp lệ");
        }

        report.setUser(currentUser);
        report.setProperty(currentProperty);

        return report;
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