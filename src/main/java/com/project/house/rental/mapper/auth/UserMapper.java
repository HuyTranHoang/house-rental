package com.project.house.rental.mapper.auth;

import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "isNonLocked", source = "nonLocked")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRoleDtos")
    @Mapping(target = "authorities", source = "roles", qualifiedByName = "toAuthorityDtos")
    UserEntityDto toDto(UserEntity user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity toEntity(UserEntityDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateEntityFromDto(UserEntityDto userDto, @MappingTarget UserEntity user);

    @Named("toRoleDtos")
    default List<String> toRoleDtos(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }

    @Named("toAuthorityDtos")
    default List<String> toAuthorityDtos(List<Role> roles) {
        return roles.stream()
                .map(Role::getAuthorities)
                .flatMap(authorityList -> authorityList.stream().map(Authority::getPrivilege))
                .distinct()
                .toList();
    }
}