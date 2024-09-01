package com.project.house.rental.service.auth;

import com.project.house.rental.constant.SecurityConstant;
import com.project.house.rental.entity.auth.RefreshToken;
import com.project.house.rental.repository.auth.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(new Date(System.currentTimeMillis() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.ofNullable(refreshTokenRepository.findByToken(token));
    }

    @Override
    public void deleteByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }
    }
}
