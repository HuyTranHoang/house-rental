package com.project.house.rental.controller;

import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.dto.params.RoleParams;
import com.project.house.rental.service.auth.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getRolesWithParams(@ModelAttribute RoleParams roleParams) {
        Map<String, Object> rolesWithPagination = roleService.getAllRolesWithParams(roleParams);
        return ResponseEntity.ok(rolesWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roleDtos = roleService.getAllRoles();
        return ResponseEntity.ok(roleDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDto);
    }

    @PreAuthorize("hasAnyAuthority('role:create', 'admin:all')")
    @PostMapping({"", "/"})
    public ResponseEntity<RoleDto> createRole(@RequestBody @Valid RoleDto roleDto) {
        RoleDto createdRoleDto = roleService.createRole(roleDto);
        return ResponseEntity.ok(createdRoleDto);
    }

    @PreAuthorize("hasAnyAuthority('role:delete', 'admin:all')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable long id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('role:delete', 'admin:all')")
    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleRoles(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        roleService.deleteMultipleRoles(ids);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('role:update', 'admin:all')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable long id, @RequestBody @Valid RoleDto roleDto) {
        RoleDto updatedRoleDto = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(updatedRoleDto);
    }
}
