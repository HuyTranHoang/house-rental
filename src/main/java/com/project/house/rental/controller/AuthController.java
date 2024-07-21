package com.project.house.rental.controller;

import com.project.house.rental.constant.SecurityConstant;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserPrincipal;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.auth.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserEntityDto> register(@RequestBody UserEntityDto user) throws CustomRuntimeException {
        UserEntityDto newUserEntityDto = userService.register(user);
        return ResponseEntity.ok(newUserEntityDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntityDto> login(@RequestBody UserEntityDto user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        UserEntity loginUser = userRepository.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        String JwtToken = jwtTokenProvider.generateJwtToken(userPrincipal);
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(SecurityConstant.JWT_TOKEN_HEADER, JwtToken);

        return ResponseEntity.ok()
                .headers(jwtHeader)
                .body(userService.toDto(loginUser));
    }
}
