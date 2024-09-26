package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.CommentDto;
import com.project.house.rental.dto.params.CommentParams;
import com.project.house.rental.entity.Comment;
import com.project.house.rental.entity.Comment_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.mapper.CommentMapper;
import com.project.house.rental.repository.CommentRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.CommentService;
import com.project.house.rental.specification.CommoentSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, HibernateFilterHelper hibernateFilterHelper, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.commentMapper = commentMapper;
    }


    @Override
    public List<CommentDto> getAllComments() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        List<Comment> comments = commentRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        return comments.stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto getCommentById(long id) {

        Comment comment = commentRepository.findByIdWithFilter(id);

        if (comment == null) {
            throw new RuntimeException("Không tìm thấy nhận xét với id = " + id);
        }

        return commentMapper.toDto(comment);

    }

    @Override
    public CommentDto createComment(CommentDto commentDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để nhận xét!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        commentDto.setUserId(user.getId());

        Comment comment = commentMapper.toEntity(commentDto);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto updateComment(long id, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdWithFilter(id);

        if (comment == null) {
            throw new NoResultException("Không tìm thấy nhận xét");
        }

        commentMapper.updateEntityFromDto(commentDto, comment);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteCommentById(long id) {
        Comment Comment = commentRepository.findByIdWithFilter(id);

        if (Comment == null) {
            throw new NoResultException("Không tìm thấy nhận xét");
        }

        commentRepository.deleteById(Comment.getId());
    }

    @Override
    public void deleteMultipleComment(List<Long> ids) {
        List<Comment> commentList = commentRepository.findAllById(ids);
        commentRepository.deleteAll(commentList);
    }

    @Override
    public Map<String, Object> getAllCommentWithParams(CommentParams commentParams) {
        Specification<Comment> spec = CommoentSpecification.filterByPropertyId(commentParams.getPropertyId())
                .and(CommoentSpecification.filterByUserId(commentParams.getUserId()))
                .and(CommoentSpecification.searchByUsernamePropertyTitle(commentParams.getSearch()));

        Sort sort = switch (commentParams.getSortBy()) {
            case "createdAtAsc" -> Sort.by(Comment_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Comment_.CREATED_AT).descending();
            default -> Sort.by(Comment_.ID).descending();
        };

        if (commentParams.getPageNumber() < 0) {
            commentParams.setPageNumber(0);
        }

        if (commentParams.getPageSize() <= 0) {
            commentParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                commentParams.getPageNumber(),
                commentParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        Page<Comment> reviewPage = commentRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        PageInfo pageInfo = new PageInfo(reviewPage);

        List<CommentDto> commentDtoList = reviewPage.stream()
                .map(commentMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", commentDtoList
        );
    }

}
