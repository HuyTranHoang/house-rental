package com.project.house.rental.repository;

import com.project.house.rental.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Query("SELECT r FROM Comment r WHERE r.id = :id AND r.isDeleted = false")
    Comment findByIdWithFilter(long id);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long countCommentsCreatedBetween(Date startDate, Date endDate);
    long count();
    @Query("SELECT COUNT(c) FROM Comment c WHERE EXTRACT(MONTH FROM c.createdAt) = :month AND EXTRACT(YEAR FROM c.createdAt) = :year")
    long countByCreatedAtMonthAndYear(@Param("month") int month, @Param("year") int year);
}
