package com.project.house.rental.controller;

import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.dto.params.MembershipParams;
import com.project.house.rental.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {
    @Autowired
    private MembershipService membershipService;

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllMemberships(@ModelAttribute MembershipParams membershipParams) {
        Map<String, Object> membershipsWithPagination = membershipService.getAllMembershipsWithParams(membershipParams);
        return ResponseEntity.ok(membershipsWithPagination);
    }

    @GetMapping("/all")
    public List<MembershipDto> getAllMembershipsNoPaging() {
        return membershipService.getAllMemberships();
    }
}
