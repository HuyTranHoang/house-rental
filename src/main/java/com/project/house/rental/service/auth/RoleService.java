package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.entity.auth.Role;

import java.util.List;

public interface RoleService {

    List<RoleDto> getAll();

    RoleDto getById(long id);

    RoleDto create(RoleDto roleDto);

    RoleDto update(long id, RoleDto roleDto);

    void deleteById(long id);

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    void updateEntityFromDto(Role role, RoleDto roleDto);
}
