package com.project.house.rental.repository;

import com.project.house.rental.entity.Report;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends GenericRepository<Report>, JpaSpecificationExecutor<Report> {
}
