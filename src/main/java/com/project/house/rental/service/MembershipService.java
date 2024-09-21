package com.project.house.rental.service;

import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.MembershipParams;
import com.project.house.rental.exception.CustomRuntimeException;

import java.util.List;
import java.util.Map;

public interface MembershipService {
    List<MembershipDto> getAllMemberships();
    Map<String, Object> getAllMembershipsWithParams(MembershipParams membershipParams);
    MembershipDto getMembershipById(long id);
    MembershipDto updateMembership(long id, MembershipDto membershipDto);
    void deleteMembershipById(long id);
    void deleteMultipleMemberships(List<Long> ids);
    MembershipDto createMembership(MembershipDto membershipDto);
    void updatePrice(long id, double price) throws CustomRuntimeException;

}
