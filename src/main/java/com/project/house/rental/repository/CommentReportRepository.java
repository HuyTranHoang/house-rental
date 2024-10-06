package com.project.house.rental.repository;

import com.project.house.rental.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long>, JpaSpecificationExecutor<CommentReport> {

    @Query("SELECT r FROM CommentReport r WHERE r.id = :id AND r.isDeleted = false")
    CommentReport findByIdWithFilter(long id);

    List<CommentReport> findAllByCommentIdAndStatus(long commentId, CommentReport.ReportStatus status);

    @Query("SELECT COUNT(r) FROM CommentReport r WHERE r.status = :status AND r.isDeleted = false")
    long countReportsWithStatus(CommentReport.ReportStatus status);

}
