package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.AuthorityDto;
import com.project.house.rental.mapper.auth.AuthorityMapper;
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
                .map(AuthorityMapper.INSTANCE::toDto)
                .toList();
    }

}
