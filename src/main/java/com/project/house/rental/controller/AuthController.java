package com.project.house.rental.controller;

import com.project.house.rental.constant.SecurityConstant;
import com.project.house.rental.dto.auth.LoginDto;
import com.project.house.rental.dto.auth.ResetPasswordDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.RefreshToken;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserPrincipal;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.mapper.auth.UserMapper;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.UserMembershipService;
import com.project.house.rental.service.auth.AuthService;
import com.project.house.rental.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final UserMembershipService userMembershipService;

    public AuthController(AuthService authService, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, UserMapper userMapper, UserMembershipService userMembershipService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
        this.userMembershipService = userMembershipService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserEntityDto> register(@RequestBody @Valid UserEntityDto user) throws CustomRuntimeException {
        UserEntityDto newUserEntityDto = authService.register(user);
        userMembershipService.createUserMembership(newUserEntityDto.getId());
        return ResponseEntity.ok(newUserEntityDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntityDto> login(@RequestBody @Valid LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        UserEntity loginUser = userRepository.findUserByUsername(loginDto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        refreshTokenService.createRefreshToken(loginUser.getId(), refreshToken);

        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(SecurityConstant.JWT_TOKEN_HEADER, accessToken);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME)
                .build();

        return ResponseEntity.ok()
                .headers(jwtHeader)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(userMapper.toDto(loginUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Logged out successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) throws CustomRuntimeException {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken token = optionalRefreshToken.get();
            UserEntity loginUser = userRepository.findUserById(token.getUserId());

            if (!loginUser.isNonLocked()) {
//                refreshTokenService.deleteByToken(refreshToken);
                throw new CustomRuntimeException("Tài khoản của bạn đã bị khóa");
            }

            UserPrincipal userPrincipal = new UserPrincipal(loginUser);
            String newAccessToken = jwtTokenProvider.generateAccessToken(userPrincipal);

            HttpHeaders jwtHeader = new HttpHeaders();
            jwtHeader.add(SecurityConstant.JWT_TOKEN_HEADER, newAccessToken);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME)
                    .build();
            jwtHeader.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return ResponseEntity.ok()
                    .headers(jwtHeader)
                    .body(userMapper.toDto(loginUser));
        } else {
            throw new CustomRuntimeException("Refresh token không hợp lệ");
        }
    }

    @PostMapping("/send-reset-password-email")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestParam @NotEmpty(message = "Vui lòng nhập email") String email) throws CustomRuntimeException {
        authService.sendEmailResetPassword(email);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserEntityDto> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) throws CustomRuntimeException {
        UserEntityDto userEntityDto = authService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(userEntityDto);
    }
}
