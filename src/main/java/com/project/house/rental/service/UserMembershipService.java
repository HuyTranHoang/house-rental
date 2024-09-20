package com.project.house.rental.service;

import com.project.house.rental.dto.DistrictDto;
import com.project.house.rental.dto.UserMembershipDto;
import com.project.house.rental.dto.params.DistrictParams;
import com.project.house.rental.dto.params.UserMembershipParams;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface UserMembershipService {
    List<UserMembershipDto> getAllUserMemberships(long membershipId);

    UserMembershipDto getUserMembershipById(long id);

    UserMembershipDto createUserMembership(long userId);

    UserMembershipDto updateUserMembership(UserMembershipDto userMembershipDto);

    UserMembershipDto getUserMembershipByUsername(String username);
}
