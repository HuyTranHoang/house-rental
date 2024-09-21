package com.project.house.rental.service;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    List<ReviewDto> getAllReviews();

    ReviewDto getReviewById(long id);

    ReviewDto createReview(ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException;

    ReviewDto updateReview(long id, ReviewDto reviewDto);

    void deleteReviewById(long id);

    Map<String, Object> getAllReviewsWithParams(ReviewParams reviewParams);

    void deleteMultipleReviews(List<Long> ids);

}
