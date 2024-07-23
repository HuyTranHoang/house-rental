package com.project.house.rental.controller;

import com.project.house.rental.constant.SecurityConstant;
import com.project.house.rental.dto.auth.LoginDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserPrincipal;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntityDto> register(@RequestBody @Valid UserEntityDto user) throws CustomRuntimeException {
        UserEntityDto newUserEntityDto = userService.register(user);
        return ResponseEntity.ok(newUserEntityDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntityDto> login(@RequestBody @Valid LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        UserEntity loginUser = userRepository.findUserByUsername(loginDto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        String JwtToken = jwtTokenProvider.generateJwtToken(userPrincipal);
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(SecurityConstant.JWT_TOKEN_HEADER, JwtToken);

        return ResponseEntity.ok()
                .headers(jwtHeader)
                .body(userService.toDto(loginUser));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<UserEntityDto> validateToken(HttpServletRequest request) throws CustomRuntimeException {
        String token = extractToken(request);
        if (token == null) {
            throw new CustomRuntimeException("Token is required");
        }

        String username = jwtTokenProvider.getSubject(token);
        UserEntity loginUser = userRepository.findUserByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        String JwtToken = jwtTokenProvider.generateJwtToken(userPrincipal);
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(SecurityConstant.JWT_TOKEN_HEADER, JwtToken);

        return ResponseEntity.ok()
                .headers(jwtHeader)
                .body(userService.toDto(loginUser));
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
