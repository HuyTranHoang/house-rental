package com.project.house.rental.mapper.auth;


import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(RoleMapperDecorator.class)
public interface RoleMapper {

    @Mapping(source = "authorities", target = "authorityPrivileges", qualifiedByName = "toAuthorityDtos")
    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateEntityFromDto(RoleDto roleDto, @MappingTarget Role role);

    @Named("toAuthorityDtos")
    default List<String> toAuthorityDtos(List<Authority> authorities) {
        return authorities.stream()
                .map(Authority::getPrivilege)
                .toList();
    }
}
