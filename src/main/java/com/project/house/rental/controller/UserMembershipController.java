package com.project.house.rental.controller;

import com.project.house.rental.dto.UserMembershipDto;
import com.project.house.rental.service.UserMembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-membership")
public class UserMembershipController {
    private final UserMembershipService userMembershipService;

    public UserMembershipController(UserMembershipService userMembershipService) {
        this.userMembershipService = userMembershipService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserMembershipDto>> getAllUserMembershipsNoPaging(@RequestParam(required = false, defaultValue = "0") long membershipId) {
        List<UserMembershipDto> userMemberships = userMembershipService.getAllUserMemberships(membershipId);
        return ResponseEntity.ok(userMemberships);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserMembershipDto> createMembership(@PathVariable long userId) {
        UserMembershipDto userMembership = userMembershipService.createUserMembership(userId);
        return ResponseEntity.ok(userMembership);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserMembershipDto> updateUserMembership(@PathVariable Long userId, @RequestBody UserMembershipDto userMembershipDto) {
        userMembershipDto.setUserId(userId);
        UserMembershipDto updatedMembership = userMembershipService.updateUserMembership(userMembershipDto);
        return ResponseEntity.ok(updatedMembership);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserMembershipDto> getByUserId(@PathVariable long userId) {
        UserMembershipDto userMembership = userMembershipService.getUserMembershipByUserId(userId);
        return ResponseEntity.ok(userMembership);
    }
}
