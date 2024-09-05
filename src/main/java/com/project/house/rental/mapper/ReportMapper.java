package com.project.house.rental.mapper;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.entity.Report;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(ReportMapperDecorator.class)
public interface ReportMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "title")
    ReportDto toDto(Report report);

    Report toEntity(ReportDto reportDto);

}