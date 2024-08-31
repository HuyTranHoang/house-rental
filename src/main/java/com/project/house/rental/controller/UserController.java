package com.project.house.rental.controller;

import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.dto.params.UserParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws CustomRuntimeException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<UserEntityDto> lockUser(@PathVariable long id) throws CustomRuntimeException {
        UserEntityDto userEntityDto = userService.lockUser(id);
        return ResponseEntity.ok(userEntityDto);
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllUser(@ModelAttribute UserParams userParams) {
        Map<String, Object> usersWithPagination = userService.getAllUserWithParams(userParams);
        return ResponseEntity.ok(usersWithPagination);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityDto> getUserById(@PathVariable long id) {
        UserEntityDto userEntityDto = userService.getUserById(id);
        return ResponseEntity.ok(userEntityDto);
    }

    @PutMapping("/update-role/{id}")
    public ResponseEntity<UserEntityDto> updateRole(@PathVariable long id, @RequestBody Map<String, List<String>> requestBody) throws CustomRuntimeException {
        List<String> roles = requestBody.get("roles");
        UserEntityDto userEntityDto = userService.updateRole(id, roles);
        return ResponseEntity.ok(userEntityDto);
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleUsers(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        userService.deleteMultipleUsers(ids);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/balance/{id}")
    public ResponseEntity<Void> updateBalance(@PathVariable Long id, @RequestBody Map<String, Double> requestBody) throws CustomRuntimeException {
        double amount = requestBody.get("amount");
        userService.updateBalance(id, amount);
        return ResponseEntity.ok().build();
    }
}
