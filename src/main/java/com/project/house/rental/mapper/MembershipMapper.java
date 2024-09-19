package com.project.house.rental.mapper;

import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.entity.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);

    MembershipDto toDto(Membership membership);

    Membership toEntity(MembershipDto membershipDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(MembershipDto membershipDto, @MappingTarget Membership membership);
}
