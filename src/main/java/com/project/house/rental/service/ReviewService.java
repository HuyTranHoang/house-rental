package com.project.house.rental.service;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.entity.Review;
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

    ReviewDto toDto(Review review);

    Review toEntity(ReviewDto reviewDto);

    void updateEntityFromDto(Review review, ReviewDto reviewDto);

//    List<Review> findByHouseId(Long houseId);
//    List<Review> findByUserId(Long userId);
//    Review findByHouseIdAndUserId(Long houseId, Long userId);
}
