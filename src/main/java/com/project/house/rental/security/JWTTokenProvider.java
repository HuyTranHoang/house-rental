package com.project.house.rental.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.house.rental.constant.SecurityConstant;
import com.project.house.rental.entity.auth.UserPrincipal;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JWTTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal) {
        List<String> authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String[] claims = authorities.toArray(new String[0]);

        return JWT.create()
                .withIssuer(SecurityConstant.COMPANY)
                .withAudience(SecurityConstant.APPLICATION_NAME)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.JWT_AUTHORITIES, claims)
                .withClaim("userId", userPrincipal.getId())
                .withClaim("username", userPrincipal.getUsername())
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    private JWTVerifier getVerifier() {
        try {
            return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                    .withIssuer(SecurityConstant.COMPANY)
                    .build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getVerifier();

        Date expiration = verifier.verify(token).getExpiresAt();
        boolean isTokenExpiration = expiration.before(new Date());

        return !isTokenExpiration && StringUtils.isNotEmpty(username);
    }

    public boolean isTokenValid(String token) {
        JWTVerifier verifier = getVerifier();

        Date expiration = verifier.verify(token).getExpiresAt();
        boolean isTokenExpiration = expiration.before(new Date());

        return !isTokenExpiration;
    }

    public List<GrantedAuthority> grantedAuthorities(String token) {
        JWTVerifier verifier = getVerifier();

        String[] claims = verifier.verify(token)
                .getClaim(SecurityConstant.JWT_AUTHORITIES)
                .asArray(String.class);

        return Stream.of(claims)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getVerifier();

        return verifier.verify(token).getSubject();
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        return usernamePasswordAuthenticationToken;
    }

    public String generatePasswordResetToken(String username) {
        return JWT.create()
                .withIssuer(SecurityConstant.COMPANY)
                .withAudience(SecurityConstant.APPLICATION_NAME)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.PASSWORD_RESET_EXPIRATION_TIME))
                .withSubject(username)
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public Date getPasswordResetTokenExpiration() {
        return new Date(System.currentTimeMillis() + SecurityConstant.PASSWORD_RESET_EXPIRATION_TIME);
    }

    public Date getCurrentDate() {
        return new Date();
    }

}
