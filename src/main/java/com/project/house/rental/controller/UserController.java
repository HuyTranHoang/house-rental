package com.project.house.rental.controller;

import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('user:update')")
    @PutMapping("/profile")
    public ResponseEntity<UserEntityDto> updateProfile(@RequestBody @Valid ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.updateProfile(profileDto, request);
        return ResponseEntity.ok(userEntityDto);
    }
}
