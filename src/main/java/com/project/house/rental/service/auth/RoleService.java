package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.entity.auth.Role;

import java.util.List;

public interface RoleService {

    List<RoleDto> getAllRoles();

    RoleDto getRoleById(long id);

    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(long id, RoleDto roleDto);

    void deleteRoleById(long id);

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    void updateEntityFromDto(Role role, RoleDto roleDto);
}
