package com.project.house.rental.repository;

import com.project.house.rental.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    @Query("SELECT r FROM Report r WHERE r.id = :id AND r.isDeleted = false")
    Report findByIdWithFilter(long id);

    List<Report> findAllByPropertyIdAndStatus(Long propertyId, Report.ReportStatus status);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    long countReportsWithStatus(Report.ReportStatus status);

}
