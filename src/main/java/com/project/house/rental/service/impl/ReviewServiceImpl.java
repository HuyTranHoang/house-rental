package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.entity.Amenity;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.Review_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.ReviewRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.ReviewService;
import com.project.house.rental.specification.ReviewSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final HibernateFilterHelper hibernateFilterHelper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, PropertyRepository propertyRepository, JWTTokenProvider jwtTokenProvider, HibernateFilterHelper hibernateFilterHelper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }


    @Override
    public List<ReviewDto> getAllReviews() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        List<Review> reviews = reviewRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        return reviews.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ReviewDto getReviewById(long id) {

        Review review = reviewRepository.findByIdWithFilter(id);

        if (review == null) {
            throw new RuntimeException("Không tìm thấy nhận xét với id = " + id);
        }

        return toDto(review);

    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để nhận xét!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        reviewDto.setUserId(user.getId());

        Review review = toEntity(reviewDto);
        review = reviewRepository.save(review);
        return toDto(review);
    }

    @Override
    public ReviewDto updateReview(long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findByIdWithFilter(id);

        if (review == null) {
            throw new RuntimeException("Không tìm thấy nhận xét");
        }

        updateEntityFromDto(review, reviewDto);
        review = reviewRepository.save(review);
        return toDto(review);
    }

    @Override
    public void deleteReviewById(long id) {
        Review review = reviewRepository.findByIdWithFilter(id);

        if (review == null) {
            throw new RuntimeException("Không tìm thấy nhận xét");
        }

        reviewRepository.deleteById(review.getId());
    }

    @Override
    public void deleteMultipleReviews(List<Long> ids) {
        List<Review> reviewList = reviewRepository.findAllById(ids);
        reviewRepository.deleteAll(reviewList);
    }

    @Override
    public Map<String, Object> getAllReviewsWithParams(ReviewParams reviewParams) {
        Specification<Review> spec = ReviewSpecification.filterByRating(reviewParams.getRating())
                .and(ReviewSpecification.filterByPropertyId(reviewParams.getPropertyId()))
                .and(ReviewSpecification.filterByUsername(reviewParams.getUserName()));

        Sort sort = switch (reviewParams.getSortBy()) {
            case "createdAtAsc" -> Sort.by(Review_.CREATED_AT);
            case "ratingAsc" -> Sort.by(Review_.RATING);
            case "ratingDesc" -> Sort.by(Review_.RATING).descending();
            default -> Sort.by(Review_.CREATED_AT).descending();
        };

        if (reviewParams.getPageNumber() < 0) {
            reviewParams.setPageNumber(0);
        }

        if (reviewParams.getPageSize() <= 0) {
            reviewParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                reviewParams.getPageNumber(),
                reviewParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        Page<Review> reviewPage = reviewRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        PageInfo pageInfo = new PageInfo(reviewPage);

        List<ReviewDto> reviewDtoList = reviewPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", reviewDtoList
        );
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
                .createdAt(review.getCreatedAt())
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
