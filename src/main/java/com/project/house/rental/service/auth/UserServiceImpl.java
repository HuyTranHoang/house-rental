package com.project.house.rental.service.auth;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.ResetPasswordDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.dto.params.UserParams;
import com.project.house.rental.entity.City_;
import com.project.house.rental.entity.auth.*;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.exception.UserAccountLockedException;
import com.project.house.rental.mapper.auth.UserMapper;
import com.project.house.rental.repository.auth.PasswordResetRepository;
import com.project.house.rental.repository.auth.RoleRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.service.email.EmailSenderService;
import com.project.house.rental.specification.UserSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final CloudinaryService cloudinaryService;
    private final PasswordResetRepository passwordResetRepository;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider, CloudinaryService cloudinaryService, PasswordResetRepository passwordResetRepository, HibernateFilterHelper hibernateFilterHelper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cloudinaryService = cloudinaryService;
        this.passwordResetRepository = passwordResetRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.userMapper = userMapper;
    }

    @Override
    public Map<String, Object> getAllUserWithParams(UserParams userParams) {
        Specification<UserEntity> spec = UserSpecification.searchByUsernameEmailPhone(userParams.getSearch())
                .and(UserSpecification.filterByRoles(userParams.getRoles(), roleRepository))
                .and(UserSpecification.filterByIsNonLocked(userParams.getIsNonLocked()));

        Sort sort = switch (userParams.getSortBy()) {
            case "usernameAsc" -> Sort.by(UserEntity_.USERNAME);
            case "usernameDesc" -> Sort.by(UserEntity_.USERNAME).descending();
            case "emailAsc" -> Sort.by(UserEntity_.EMAIL);
            case "emailDesc" -> Sort.by(UserEntity_.EMAIL).descending();
            case "phoneNumbeAscr" -> Sort.by(UserEntity_.PHONE_NUMBER);
            case "phoneNumbeDesc" -> Sort.by(UserEntity_.PHONE_NUMBER).descending();
            case "isNonLockedAsc" -> Sort.by(UserEntity_.IS_NON_LOCKED);
            case "isNonLockedDesc" -> Sort.by(UserEntity_.IS_NON_LOCKED).descending();
            case "createdAtAsc" -> Sort.by(UserEntity_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(UserEntity_.CREATED_AT).descending();
            default -> Sort.by(City_.ID).descending();
        };

        if (userParams.getPageNumber() < 0) {
            userParams.setPageNumber(0);
        }

        if (userParams.getPageSize() <= 0) {
            userParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                userParams.getPageNumber(),
                userParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_USER_FILTER);

        Page<UserEntity> userPage = userRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_USER_FILTER);

        PageInfo pageInfo = new PageInfo(userPage);

        List<UserEntityDto> userEntityDtoList = userPage.stream()
                .map(userMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", userEntityDtoList
        );
    }

    @Override
    public UserEntityDto getUserById(long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy tài khoản!"));

        return userMapper.toDto(user);
    }

    @Override
    public UserEntityDto addNewUser(UserEntityDto user) throws CustomRuntimeException {
        UserEntity existUsername = userRepository.findUserByUsername(user.getUsername());
        if (existUsername != null) {
            throw new CustomRuntimeException("Tài khoản đã tồn tại!");
        }

        UserEntity existEmail = userRepository.findUserByEmail(user.getEmail());
        if (existEmail != null) {
            throw new CustomRuntimeException("Email đã được đăng ký!");
        }

        List<Role> roles = user.getRoles().stream()
                .map(roleName -> {
                    Role role = roleRepository.findRoleByNameIgnoreCase(roleName);
                    if (role == null) {
                        throw new IllegalArgumentException("Không tìm thấy quyền: " + roleName);
                    }
                    return role;
                })
                .toList();

        if (roles.isEmpty() || roles.contains(null)) {
            throw new CustomRuntimeException("Vui lòng chọn ít nhất một quyền hợp lệ cho tài khoản!");
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        user.setActive(true);
        user.setNonLocked(true);
        user.setRoles(user.getRoles());

        //TODO: Bật lên khi demo
        //emailSenderService.sendRegisterHTMLMail(user.getEmail());

        UserEntity newUser = userMapper.toEntity(user);
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserEntityDto updateUser(long id, UserEntityDto user) throws CustomRuntimeException {
        UserEntity existUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Không tìm thấy tài khoản!"));

        userMapper.updateEntityFromDto(user, existUser);
        return userMapper.toDto(userRepository.save(existUser));
    }

    @Override
    public UserEntityDto lockUser(long id) throws CustomRuntimeException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Không tìm thấy tài khoản!"));

        user.setNonLocked(!user.isNonLocked());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void sendEmailResetPassword(String email) throws CustomRuntimeException {
        UserEntity user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản với email này!");
        }

        PasswordReset passwordReset = passwordResetRepository.findByUserId(user.getId());

        if (passwordReset != null) {
            passwordResetRepository.delete(passwordReset);
        }

        PasswordReset newPasswordReset = PasswordReset.builder()
                .token(jwtTokenProvider.generatePasswordResetToken(user.getUsername()))
                .user(user)
                .expiresAt(jwtTokenProvider.getPasswordResetTokenExpiration())
                .isUsed(false)
                .build();

        passwordResetRepository.save(newPasswordReset);
        emailSenderService.sendResetPasswordHTMLMail(user.getEmail(), newPasswordReset.getToken());
    }

    @Override
    public UserEntityDto resetPassword(ResetPasswordDto resetPasswordDto) throws CustomRuntimeException {
        String token = resetPasswordDto.getToken();
        String password = resetPasswordDto.getNewPassword();

        PasswordReset passwordReset = passwordResetRepository.findByToken(token);

        if (passwordReset == null) {
            throw new CustomRuntimeException("Token không hợp lệ!");
        }

        if (passwordReset.isUsed()) {
            throw new CustomRuntimeException("Token đã được sử dụng!");
        }

        if (passwordReset.getExpiresAt().before(jwtTokenProvider.getCurrentDate())) {
            throw new CustomRuntimeException("Token đã hết hạn!");
        }

        UserEntity user = passwordReset.getUser();

        if (resetPasswordDto.getEmail() != null && !resetPasswordDto.getEmail().equals(user.getEmail())) {
            throw new CustomRuntimeException("Email không khớp với tài khoản!");
        }

        user.setPassword(passwordEncoder.encode(password));
        passwordReset.setUsed(true);
        passwordResetRepository.save(passwordReset);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) throws CustomRuntimeException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Không tìm thấy tài khoản!"));

        userRepository.delete(user);
    }

    @Override
    public void deleteMultipleUsers(List<Long> ids) {
        List<UserEntity> reviewList = userRepository.findAllById(ids);
        userRepository.deleteAll(reviewList);
    }

    @Override
    public void updateBalance(long id, double amount) throws CustomRuntimeException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Không tìm thấy tài khoản!"));

        double newBalance = user.getBalance() + amount;

        if (newBalance < 0) {
            throw new CustomRuntimeException("Số dư không thể nhỏ hơn 0!");
        }

        user.setBalance(newBalance);
        userRepository.save(user);
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

        //TODO: Bật lên khi demo
//        emailSenderService.sendRegisterHTMLMail(user.getEmail());

        UserEntity newUser = userMapper.toEntity(user);
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserEntityDto updateRole(long id, List<String> roles) throws CustomRuntimeException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException("Không tìm thấy tài khoản!"));

        List<Role> rolesUpdate = new ArrayList<>(roles.stream()
                .map(roleName -> {
                    Role role = roleRepository.findRoleByNameIgnoreCase(roleName);
                    if (role == null) {
                        throw new IllegalArgumentException("Không tìm thấy quyền: " + roleName);
                    }
                    return role;
                })
                .toList());

        if (roles.isEmpty() || roles.contains(null)) {
            throw new CustomRuntimeException("Vui lòng chọn ít nhất một quyền hợp lệ cho tài khoản!");
        }

        user.setRoles(rolesUpdate);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản!");
        }

        if (!user.isNonLocked()) {
            throw new UserAccountLockedException("Tài khoản đã bị khóa!");
        }

        return new UserPrincipal(user);
    }

    @Override
    public UserEntityDto updateProfile(ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
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

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserEntityDto changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
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

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserEntityDto updateAvatar(MultipartFile avatar, HttpServletRequest request) throws CustomRuntimeException, IOException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        if (username == null) {
            throw new CustomRuntimeException("Vui lòng đăng nhập để thay đổi ảnh đại diện!");
        }

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new CustomRuntimeException("Không tìm thấy tài khoản!");
        }

        String oldAvatarUrl = user.getAvatarUrl();
        int index = oldAvatarUrl.indexOf("v1/");
        if (index != -1) {
            String publicId = oldAvatarUrl.substring(index + 3);
            cloudinaryService.delete(publicId);
        }

        String publicId = String.format("avatar/%s", UUID.randomUUID());

        Map cloudinaryResponse = cloudinaryService.upload(avatar, publicId);

        String avatarPublicId = (String) cloudinaryResponse.get("public_id");

        String optimizedUrl = cloudinaryService.getOptimizedImage(avatarPublicId);

        user.setAvatarUrl(optimizedUrl);

        return userMapper.toDto(userRepository.save(user));
    }

}
