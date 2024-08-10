package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.AuthorityDto;
import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.repository.auth.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<AuthorityDto> getAllAuthorities() {
        return authorityRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public AuthorityDto toDto(Authority authority) {
        return AuthorityDto.builder()
                .id(authority.getId())
                .privilege(authority.getPrivilege())
                .createdAt(authority.getCreatedAt())
                .build();
    }
}
