package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.dto.params.RoleParams;
import com.project.house.rental.entity.auth.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<RoleDto> getAllRoles();

    Map<String, Object> getAllRolesWithParams(RoleParams roleParams);

    RoleDto getRoleById(long id);

    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(long id, RoleDto roleDto);

    void deleteRoleById(long id);

    void deleteMultipleRoles(List<Long> ids);

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    void updateEntityFromDto(Role role, RoleDto roleDto);
}
