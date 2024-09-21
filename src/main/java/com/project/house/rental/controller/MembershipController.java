package com.project.house.rental.controller;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.MembershipDto;
import com.project.house.rental.dto.params.CityParams;
import com.project.house.rental.dto.params.MembershipParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.MembershipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDto> getMembershipById(@PathVariable long id) {
        MembershipDto membershipDto = membershipService.getMembershipById(id);
        return ResponseEntity.ok(membershipDto);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<MembershipDto> createMembership(@RequestBody @Valid MembershipDto membershipDto) {
        MembershipDto membership = membershipService.createMembership(membershipDto);
        return ResponseEntity.ok(membership);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipDto> updateMembership(@PathVariable long id, @RequestBody @Valid MembershipDto membershipDto) {
        MembershipDto membership = membershipService.updateMembership(id, membershipDto);
        return ResponseEntity.ok(membership);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable long id) {
        membershipService.deleteMembershipById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultipleMemberships(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> ids = requestBody.get("ids");
        membershipService.deleteMultipleMemberships(ids);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/price/{id}")
    public ResponseEntity<Void> updatePrice(@PathVariable long id, @RequestBody Map<String, Double> requestBody) throws CustomRuntimeException {
        double price = requestBody.get("price");
        membershipService.updatePrice(id, price);
        return ResponseEntity.ok().build();
    }
}
