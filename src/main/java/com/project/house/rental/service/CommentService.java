package com.project.house.rental.service;

import com.project.house.rental.dto.CommentDto;
import com.project.house.rental.dto.params.CommentParams;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface CommentService {

    List<CommentDto> getAllComments();

    CommentDto getCommentById(long id);

    CommentDto createComment(CommentDto commentDto, HttpServletRequest request) throws CustomRuntimeException;

    CommentDto updateComment(long id, CommentDto commentDto);

    void deleteCommentById(long id);

    Map<String, Object> getAllCommentWithParams(CommentParams commentParams);

    void deleteMultipleComment(List<Long> ids);

}
