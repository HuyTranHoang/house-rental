package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.AuthorityDto;

import java.util.List;

public interface AuthorityService {
    List<AuthorityDto> getAllAuthorities();
}
