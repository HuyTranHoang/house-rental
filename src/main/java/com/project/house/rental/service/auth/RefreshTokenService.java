package com.project.house.rental.service.auth;

import com.project.house.rental.entity.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    void createRefreshToken(long userId, String token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
