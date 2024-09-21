package com.project.house.rental.mapper;

import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RoomTypeMapper {

    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    RoomTypeDto toDto(RoomType roomType);

    RoomType toEntity(RoomTypeDto roomTypeDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(RoomTypeDto roomTypeDto, @MappingTarget RoomType roomType);
}
