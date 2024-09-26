package com.project.house.rental.mapper;

import com.project.house.rental.dto.CommentDto;
import com.project.house.rental.entity.Comment;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.auth.UserRepository;
import jakarta.persistence.NoResultException;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CommentMapperDecorator implements CommentMapper {

    @Autowired
    @Qualifier("delegate")
    private CommentMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Comment toEntity(CommentDto commentDto) {
        Comment comment = delegate.toEntity(commentDto);

        UserEntity user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + commentDto.getUserId()));

        Property property = propertyRepository.findById(commentDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy property với id: " + commentDto.getPropertyId()));

        comment.setUser(user);
        comment.setProperty(property);
        return comment;
    }

    @Override
    public void updateEntityFromDto(CommentDto commentDto, @MappingTarget Comment comment) {
        delegate.updateEntityFromDto(commentDto, comment);

        UserEntity user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy user với id: " + commentDto.getUserId()));

        Property property = propertyRepository.findById(commentDto.getPropertyId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy property với id: " + commentDto.getPropertyId()));

        comment.setUser(user);
        comment.setProperty(property);
    }
}