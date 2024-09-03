package com.project.house.rental.service.impl;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.Review_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.mapper.ReviewMapper;
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
    private final JWTTokenProvider jwtTokenProvider;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, HibernateFilterHelper hibernateFilterHelper, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.reviewMapper = reviewMapper;
    }


    @Override
    public List<ReviewDto> getAllReviews() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        List<Review> reviews = reviewRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_REVIEW_FILTER);

        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public ReviewDto getReviewById(long id) {

        Review review = reviewRepository.findByIdWithFilter(id);

        if (review == null) {
            throw new RuntimeException("Không tìm thấy nhận xét với id = " + id);
        }

        return reviewMapper.toDto(review);

    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để nhận xét!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        reviewDto.setUserId(user.getId());

        Review review = reviewMapper.toEntity(reviewDto);
        review = reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto updateReview(long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findByIdWithFilter(id);

        if (review == null) {
            throw new RuntimeException("Không tìm thấy nhận xét");
        }

        reviewMapper.updateEntityFromDto(reviewDto, review);
        review = reviewRepository.save(review);
        return reviewMapper.toDto(review);
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
                .and(ReviewSpecification.filterByUserId(reviewParams.getUserId()))
                .and(ReviewSpecification.searchByUsernamePropertyTitle(reviewParams.getSearch()));

        Sort sort = switch (reviewParams.getSortBy()) {
            case "ratingAsc" -> Sort.by(Review_.RATING);
            case "ratingDesc" -> Sort.by(Review_.RATING).descending();
            case "createdAtAsc" -> Sort.by(Review_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Review_.CREATED_AT).descending();
            default -> Sort.by(Review_.ID).descending();
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
                .map(reviewMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", reviewDtoList
        );
    }

}
