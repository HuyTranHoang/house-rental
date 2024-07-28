package com.project.house.rental.service.auth.impl;

import com.project.house.rental.common.email.EmailSenderService;
import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserPrincipal;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.auth.RoleRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserEntityDto addNewUser(UserEntityDto user, String[] role) {
        return null;
    }

    @Override
    public UserEntityDto updateUser(UserEntityDto user, String[] role) {
        return null;
    }

    @Override
    public void resetPassword(String email) throws CustomRuntimeException {

    }

    @Override
    public void deleteUser(long id) throws CustomRuntimeException {

    }

    @Override
    public UserEntityDto register(UserEntityDto user) throws CustomRuntimeException {

        UserEntity existUsername = userRepository.findUserByUsername(user.getUsername());
        if (existUsername != null) {
            throw new CustomRuntimeException("Tài khoản đã tồn tại!");
        }

        UserEntity existEmail = userRepository.findUserByEmail(user.getEmail());
        if (existEmail != null) {
            throw new CustomRuntimeException("Email đã được đăng ký!");
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        user.setActive(true);
        user.setNonLocked(true);
        user.setRoles(List.of("ROLE_USER"));

//        emailSenderService.sendRegisterHTMLMail(user.getEmail());

        UserEntity newUser = toEntity(user);
        return toDto(userRepository.save(newUser));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản!");
        }

        if (!user.isNonLocked()) {
            throw new UsernameNotFoundException("Tài khoản đã bị khóa!");
        }

        return new UserPrincipal(user);
    }

    @Override
    public UserEntityDto updateProfile(ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để thay đổi thông tin tài khoản!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setPhoneNumber(profileDto.getPhoneNumber());

        return toDto(userRepository.save(user));
    }

    @Override
    public UserEntityDto changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để thay đổi mật khẩu!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new CustomRuntimeException("Mật khẩu cũ không chính xác!");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        return toDto(userRepository.save(user));
    }

    /*
        Helper methods
     */

    public UserEntityDto toDto(UserEntity user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        List<String> authorities = user.getRoles().stream()
                .map(Role::getAuthorities)
                .flatMap(authorityList -> authorityList.stream().map(Authority::getPrivilege))
                .distinct()
                .toList();

        return UserEntityDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.isActive())
                .isNonLocked(user.isNonLocked())
                .roles(roles)
                .authorities(authorities)
                .build();
    }

    private UserEntity toEntity(UserEntityDto userDto) {
        List<Role> roles = userDto.getRoles().stream()
                .map(roleRepository::findRoleByName)
                .toList();

        List<Authority> authorities = userDto.getRoles().stream()
                .map(roleRepository::findRoleByName)
                .flatMap(role -> role.getAuthorities().stream())
                .distinct()
                .toList();

        return UserEntity.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .avatarUrl(userDto.getAvatarUrl())
                .isActive(userDto.isActive())
                .isNonLocked(userDto.isNonLocked())
                .roles(roles)
                .authorities(authorities)
                .build();
    }

    private void updateEntityFromDto(UserEntity user, UserEntityDto userDto) {
        List<Role> roles = userDto.getRoles().stream()
                .map(roleRepository::findRoleByName)
                .toList();

        List<Authority> authorities = userDto.getRoles().stream()
                .map(roleRepository::findRoleByName)
                .flatMap(role -> role.getAuthorities().stream())
                .distinct()
                .toList();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setActive(userDto.isActive());
        user.setNonLocked(userDto.isNonLocked());
        user.setRoles(roles);
        user.setAuthorities(authorities);
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return jwtTokenProvider.getSubject(token);
        }
        return null;
    }
}
