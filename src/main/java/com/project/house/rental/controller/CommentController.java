package com.project.house.rental.controller;

import com.project.house.rental.dto.CommentDto;
import com.project.house.rental.dto.params.CommentParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllCommentsWithParams(@ModelAttribute CommentParams commentParams) {
        Map<String, Object> reviewsWithParams = commentService.getAllCommentWithParams(commentParams);
        return ResponseEntity.ok(reviewsWithParams);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> commentDtoList = commentService.getAllComments();
        return ResponseEntity.ok(commentDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentById(id);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto, HttpServletRequest request) throws CustomRuntimeException {
        CommentDto newComment = commentService.createComment(commentDto, request);
        return ResponseEntity.ok(newComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleComments(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        commentService.deleteMultipleComment(ids);
        return ResponseEntity.noContent().build();
    }
}
