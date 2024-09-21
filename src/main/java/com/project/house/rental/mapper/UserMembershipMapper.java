package com.project.house.rental.mapper;

import com.project.house.rental.dto.UserMembershipDto;
import com.project.house.rental.entity.UserMembership;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(UserMembershipMapperDecorator.class)
public interface UserMembershipMapper {
    @Mapping(source = "membership.id", target = "membershipId")
    @Mapping(source = "membership.name", target = "membershipName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    UserMembershipDto toDto(UserMembership userMembership);

    UserMembership toEntity(UserMembershipDto userMembershipDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(UserMembershipDto userMembershipDto, @MappingTarget UserMembership userMembership);
}
