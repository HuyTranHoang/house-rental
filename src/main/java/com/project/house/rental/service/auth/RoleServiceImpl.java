package com.project.house.rental.service.auth;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.dto.params.RoleParams;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.Role_;
import com.project.house.rental.exception.ConflictException;
import com.project.house.rental.mapper.auth.RoleMapper;
import com.project.house.rental.repository.auth.AuthorityRepository;
import com.project.house.rental.repository.auth.RoleRepository;
import com.project.house.rental.specification.RoleSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository, HibernateFilterHelper hibernateFilterHelper, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDto> getAllRoles() {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        List<Role> roles = roleRepository.findAll();
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        return roles.stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public Map<String, Object> getAllRolesWithParams(RoleParams roleParams) {
        Specification<Role> spec = RoleSpecification.searchByName(roleParams.getName())
                .and(RoleSpecification.filterByAuthority(roleParams.getAuthorities(), authorityRepository));

        Sort sort = switch (roleParams.getSortBy()) {
            case "nameAsc" -> Sort.by(Role_.NAME);
            case "nameDesc" -> Sort.by(Role_.NAME).descending();
            case "createdAtAsc" -> Sort.by(Role_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Role_.CREATED_AT).descending();
            default -> Sort.by(Role_.ID).descending();
        };

        if (roleParams.getPageNumber() < 0) {
            roleParams.setPageNumber(0);
        }
    
        if (roleParams.getPageSize() <= 0) {
            roleParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                roleParams.getPageNumber(),
                roleParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        PageInfo pageInfo = new PageInfo(rolePage);

        List<RoleDto> roleDtoList = rolePage.stream()
                .map(roleMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", roleDtoList
        );
    }

    @Override
    public RoleDto getRoleById(long id) {
        Role role = roleRepository.findByIdWithFilter(id);

        if (role == null) {
            throw new NoResultException("Không tìm thấy vai trò với id: " + id);
        }

        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        Role existingRole = roleRepository.findRoleByNameIgnoreCase(roleDto.getName());
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        if (existingRole != null) {
            throw new ConflictException("Vai trò đã tồn tại");
        }

        Role role = roleMapper.toEntity(roleDto);
        roleRepository.save(role);

        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto updateRole(long id, RoleDto roleDto) {
        Role role = roleRepository.findByIdWithFilter(id);

        if (role == null) {
            throw new NoResultException("Không tìm thấy vai trò với id: " + id);
        }

        if (role.getName().equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Không thể cập nhật vai trò admin");
        }

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROLE_FILTER);
        Role existingRole = roleRepository.findRoleByNameIgnoreCase(roleDto.getName());
        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROLE_FILTER);

        if (existingRole != null && existingRole.getId() != id) {
            throw new ConflictException("Vai trò đã tồn tại");
        }

        roleMapper.updateEntityFromDto(roleDto, role);
        roleRepository.save(role);

        return roleMapper.toDto(role);
    }

    @Override
    public void deleteRoleById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy vai trò với id: " + id));

        if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_USER")) {
            throw new IllegalArgumentException("Không thể xóa vai trò mặc định");
        }

        roleRepository.deleteById(role.getId());
    }

    @Override
    public void deleteMultipleRoles(List<Long> ids) {
        List<Role> roles = roleRepository.findAllById(ids);
        roleRepository.deleteAll(roles);
    }
}
