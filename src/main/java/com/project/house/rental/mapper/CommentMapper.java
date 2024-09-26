package com.project.house.rental.mapper;

import com.project.house.rental.dto.CommentDto;
import com.project.house.rental.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentMapper {

    @Mapping(source = "blocked", target = "isBlocked")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "user.avatarUrl", target = "userAvatar")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "propertyTitle")
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CommentDto commentDto, @MappingTarget Comment comment);
}