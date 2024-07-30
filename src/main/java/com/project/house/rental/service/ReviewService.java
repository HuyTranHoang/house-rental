package com.project.house.rental.service;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.entity.Review;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

public interface ReviewService extends GenericService<Review, ReviewDto> {

    ReviewDto create(ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException;

//    List<Review> findByHouseId(Long houseId);
//    List<Review> findByUserId(Long userId);
//    Review findByHouseIdAndUserId(Long houseId, Long userId);
}
