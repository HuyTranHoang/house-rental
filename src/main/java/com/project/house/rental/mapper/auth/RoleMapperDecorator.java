package com.project.house.rental.mapper.auth;


import com.project.house.rental.dto.auth.RoleDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.repository.auth.AuthorityRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public abstract class RoleMapperDecorator implements RoleMapper{

    @Autowired
    @Qualifier("delegate")
    private RoleMapper delegate;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Role toEntity(RoleDto roleDto) {
        Role role = delegate.toEntity(roleDto);

        List<Authority> authorities = authorityRepository
                .findAllByPrivilegeIn(roleDto.getAuthorityPrivileges());

        if (authorities.size() != roleDto.getAuthorityPrivileges().size()) {
            throw new NoResultException("Không tìm thấy quyền đuộc cung cấp");
        }

        role.setAuthorities(authorities);

        return role;
    }

    @Override
    public void updateEntityFromDto(RoleDto roleDto, Role role) {
        delegate.updateEntityFromDto(roleDto, role);

        List<Authority> authorities = authorityRepository
                .findAllByPrivilegeIn(roleDto.getAuthorityPrivileges());

        if (authorities.size() != roleDto.getAuthorityPrivileges().size()) {
            throw new NoResultException("Không tìm thấy quyền đuộc cung cấp");
        }

        role.getAuthorities().clear();
        role.getAuthorities().addAll(authorities);
    }
}
