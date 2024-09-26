package com.project.house.rental.mapper;

import com.project.house.rental.dto.CommentReportDto;
import com.project.house.rental.entity.Comment;
import com.project.house.rental.entity.CommentReport;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.CommentRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CommentReportMapperDecorator implements CommentReportMapper {

    @Autowired
    @Qualifier("delegate")
    private CommentReportMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentReport toEntity(CommentReportDto commentReportDto) {
        CommentReport commentReport = delegate.toEntity(commentReportDto);

        UserEntity currentUser = userRepository.findById(commentReportDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + commentReportDto.getUserId()));

        Comment currentComment = commentRepository.findByIdWithFilter(commentReportDto.getCommentId());

        if (currentComment == null) {
            throw new NoResultException("Không tìm thấy comment với id: " + commentReportDto.getCommentId());
        }

        if (!isValidReportCategory(commentReportDto.getCategory())) {
            throw new IllegalArgumentException("Danh mục [" + commentReportDto.getCategory() + "] không hợp lệ");
        }

        commentReport.setUser(currentUser);
        commentReport.setComment(currentComment);

        return commentReport;
    }


    private boolean isValidReportCategory(String cateogry) {
        try {
            CommentReport.ReportCategory.valueOf(cateogry);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}