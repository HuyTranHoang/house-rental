package com.project.house.rental.controller;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllReviewsWithParams(@ModelAttribute ReviewParams reviewParams) {
        Map<String, Object> reviewsWithParams = reviewService.getAllReviewsWithParams(reviewParams);
        return ResponseEntity.ok(reviewsWithParams);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviewDtoList = reviewService.getAll();
        return ResponseEntity.ok(reviewDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto reviewDto = reviewService.getById(id);
        return ResponseEntity.ok(reviewDto);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException {
        ReviewDto newReview = reviewService.create(reviewDto, request);
        return ResponseEntity.ok(newReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.update(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
