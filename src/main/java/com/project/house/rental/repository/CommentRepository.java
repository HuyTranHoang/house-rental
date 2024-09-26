package com.project.house.rental.repository;

import com.project.house.rental.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Query("SELECT r FROM Comment r WHERE r.id = :id AND r.isDeleted = false")
    Comment findByIdWithFilter(long id);
}
