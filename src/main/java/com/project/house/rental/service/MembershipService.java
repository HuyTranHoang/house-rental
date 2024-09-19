package com.project.house.rental.service;

import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.MembershipParams;

import java.util.List;
import java.util.Map;

public interface MembershipService {
    List<MembershipDto> getAllMemberships();
    Map<String, Object> getAllMembershipsWithParams(MembershipParams membershipParams);

}
