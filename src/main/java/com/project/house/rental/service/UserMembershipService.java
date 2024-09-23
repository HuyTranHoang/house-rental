package com.project.house.rental.service;

import com.project.house.rental.dto.UserMembershipDto;

import java.util.List;

public interface UserMembershipService {
    List<UserMembershipDto> getAllUserMemberships(long membershipId);

    UserMembershipDto getUserMembershipById(long id);

    UserMembershipDto createUserMembership(long userId);

    UserMembershipDto updateUserMembership(UserMembershipDto userMembershipDto);

    UserMembershipDto getUserMembershipByUserId(long userId);
}
