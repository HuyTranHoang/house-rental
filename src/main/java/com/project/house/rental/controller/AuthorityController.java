package com.project.house.rental.controller;

import com.project.house.rental.dto.auth.AuthorityDto;
import com.project.house.rental.service.auth.AuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }
}
