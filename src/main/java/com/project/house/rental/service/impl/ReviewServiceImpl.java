package com.project.house.rental.service.impl;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.GenericRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.ReviewRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends GenericServiceImpl<Review, ReviewDto> implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, PropertyRepository propertyRepository, JWTTokenProvider jwtTokenProvider) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected GenericRepository<Review> getRepository() {
        return reviewRepository;
    }

    @Override
    public ReviewDto create(ReviewDto reviewDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ReviewDto create(ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để thay đổi ảnh đại diện!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        reviewDto.setUserId(user.getId());

        return super.create(reviewDto);
    }

    /*
        Helper method
     */

    @Override
    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUser().getId())
                .userName(review.getUser().getUsername())
                .propertyId(review.getProperty().getId())
                .propertyTitle(review.getProperty().getTitle())
                .build();
    }

    @Override
    public Review toEntity(ReviewDto reviewDto) {
        UserEntity user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyRepository.findById(reviewDto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        return Review.builder()
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .user(user)
                .property(property)
                .build();
    }

    @Override
    public void updateEntityFromDto(Review review, ReviewDto reviewDto) {
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return jwtTokenProvider.getSubject(token);
        }
        return null;
    }

}
