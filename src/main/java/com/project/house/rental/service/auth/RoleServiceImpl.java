package com.project.house.rental.service.auth;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.repository.auth.AuthorityRepository;
import com.project.house.rental.repository.auth.RoleRepository;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public RoleServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<RoleDto> getAllRoles() {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        List<Role> roles = roleRepository.findAll();
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        return roles.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RoleDto getRoleById(long id) {
        Role role = roleRepository.findByIdWithFilter(id);

        if (role == null) {
            throw new NoResultException("Không tìm thấy vai trò với id: " + id);
        }

        return toDto(role);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        Role existingRole = roleRepository.findRoleByNameIgnoreCase(roleDto.getName());
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        if (existingRole != null) {
            throw new NoResultException("Vai trò đã tồn tại");
        }

        Role role = toEntity(roleDto);
        roleRepository.save(role);

        return toDto(role);
    }

    @Override
    public RoleDto updateRole(long id, RoleDto roleDto) {
        Role role = roleRepository.findByIdWithFilter(id);

        if (role == null) {
            throw new NoResultException("Không tìm thấy vai trò với id: " + id);
        }

        updateEntityFromDto(role, roleDto);
        roleRepository.save(role);

        return toDto(role);
    }

    @Override
    public void deleteRoleById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy 'City' với id = " + id));

        roleRepository.deleteById(role.getId());
    }

    @Override
    public RoleDto toDto(Role role) {
        List<String> authorityPrivileges = role.getAuthorities().stream()
                .map(Authority::getPrivilege)
                .toList();

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .authorityPrivileges(authorityPrivileges)
                .build();
    }

    @Override
    public Role toEntity(RoleDto roleDto) {
        List<Authority> authorities = authorityRepository
                .findAllByPrivilegeIn(roleDto.getAuthorityPrivileges());

        if (authorities.size() != roleDto.getAuthorityPrivileges().size()) {
            throw new NoResultException("Không tìm thấy quyền đuộc cung cấp");
        }

        return Role.builder()
                .name(roleDto.getName())
                .authorities(authorities)
                .build();
    }

    @Override
    public void updateEntityFromDto(Role role, RoleDto roleDto) {
        List<Authority> authorities = authorityRepository
                .findAllByPrivilegeIn(roleDto.getAuthorityPrivileges());

        role.setName(roleDto.getName());
        role.setAuthorities(authorities);
    }
}
