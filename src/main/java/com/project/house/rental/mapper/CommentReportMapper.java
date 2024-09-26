package com.project.house.rental.mapper;

import com.project.house.rental.dto.CommentReportDto;
import com.project.house.rental.entity.CommentReport;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(CommentReportMapperDecorator.class)
public interface CommentReportMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "comment.comment", target = "comment")
    CommentReportDto toDto(CommentReport commentReport);

    @Mapping(target = "comment", ignore = true)
    CommentReport toEntity(CommentReportDto commentReportDto);
}