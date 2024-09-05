package com.project.house.rental.mapper;

import com.project.house.rental.dto.ReviewDto;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Review;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ReviewMapperDecorator implements ReviewMapper {

    @Autowired
    @Qualifier("delegate")
    private ReviewMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Review toEntity(ReviewDto reviewDto) {
        Review review = delegate.toEntity(reviewDto);

        UserEntity user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + reviewDto.getUserId()));

        Property property = propertyRepository.findById(reviewDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy property với id: " + reviewDto.getPropertyId()));

        review.setUser(user);
        review.setProperty(property);
        return review;
    }

    @Override
    public void updateEntityFromDto(ReviewDto reviewDto, @MappingTarget Review review) {
        delegate.updateEntityFromDto(reviewDto, review);

        UserEntity user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + reviewDto.getUserId()));

        Property property = propertyRepository.findById(reviewDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy property với id: " + reviewDto.getPropertyId()));

        review.setUser(user);
        review.setProperty(property);
    }
}