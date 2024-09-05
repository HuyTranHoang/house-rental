package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.ResetPasswordDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.PasswordReset;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.mapper.auth.UserMapper;
import com.project.house.rental.repository.auth.PasswordResetRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.email.EmailSenderService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailSenderService emailSenderService;
    private final JWTTokenProvider jwtTokenProvider;


    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, PasswordResetRepository passwordResetRepository, EmailSenderService emailSenderService, JWTTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.passwordResetRepository = passwordResetRepository;
        this.emailSenderService = emailSenderService;
        this.jwtTokenProvider = jwtTokenProvider;
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
}
