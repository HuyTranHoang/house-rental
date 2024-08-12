package com.project.house.rental.controller;

import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('user:update', 'admin:all')")
    @PutMapping("/profile")
    public ResponseEntity<UserEntityDto> updateProfile(@RequestBody @Valid ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.updateProfile(profileDto, request);
        return ResponseEntity.ok(userEntityDto);
    }

    @PreAuthorize("hasAnyAuthority('user:update', 'admin:all')")
    @PutMapping("/change-password")
    public ResponseEntity<UserEntityDto> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.changePassword(changePasswordDto, request);
        return ResponseEntity.ok(userEntityDto);
    }

    @PreAuthorize("hasAnyAuthority('user:update', 'admin:all')")
    @PutMapping("/update-avatar")
    public ResponseEntity<UserEntityDto> updateAvatar(@RequestParam MultipartFile avatar, HttpServletRequest request) throws CustomRuntimeException, IOException {
        UserEntityDto userEntityDto = userService.updateAvatar(avatar, request);
        return ResponseEntity.ok(userEntityDto);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<UserEntityDto> addNewUser(@RequestBody @Valid UserEntityDto user) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.addNewUser(user);
        return ResponseEntity.ok(userEntityDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntityDto> updateUser(@PathVariable long id, @RequestBody @Valid UserEntityDto user) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.updateUser(id, user);
        return ResponseEntity.ok(userEntityDto);
    }

}
