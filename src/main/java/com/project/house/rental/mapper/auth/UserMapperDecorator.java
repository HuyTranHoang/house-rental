package com.project.house.rental.mapper.auth;

import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.auth.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserEntity toEntity(UserEntityDto userDto) {
        UserEntity user = delegate.toEntity(userDto);

        List<Role> roles = userDto.getRoles().stream()
                .map(roleRepository::findRoleByNameIgnoreCase)
                .toList();

        List<Authority> authorities = roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .distinct()
                .toList();

        user.setRoles(roles);
        user.setAuthorities(authorities);

        return user;
    }

    @Override
    public void updateEntityFromDto(UserEntityDto userDto, UserEntity user) {
        delegate.updateEntityFromDto(userDto, user);

        List<Role> roles = userDto.getRoles().stream()
                .map(roleName -> {
                    Role role = roleRepository.findRoleByNameIgnoreCase(roleName);
                    if (role == null) {
                        throw new IllegalArgumentException("Không tìm thấy quyền: " + roleName);
                    }
                    return role;
                })
                .toList();

        if (roles.isEmpty() || roles.contains(null)) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một quyền hợp lệ cho tài khoản!");
        }

        List<Authority> authorities = roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .distinct()
                .toList();

        user.getRoles().clear();
        user.getRoles().addAll(roles);

        user.getAuthorities().clear();
        user.getAuthorities().addAll(authorities);
    }
}