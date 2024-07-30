package com.project.house.rental.constant;

public class SecurityConstant {

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";

    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds

    public static final long PASSWORD_RESET_EXPIRATION_TIME= 3_600_000; // 1 hour expressed in milliseconds

    public static final String COMPANY = "HOUSE RENTAL";

    public static final String APPLICATION_NAME = "House Rental API";

    public static final String JWT_AUTHORITIES = "authorities";

    public static final String[] API_PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh-token",
            "/api/auth/send-reset-password-email",
            "/api/auth/reset-password",
            "/api/contact",
    };

    public static final String[] API_PUBLIC_GET_URLS = {
            "/api/city/**",
            "/api/district/**",
            "/api/property/**",
            "/api/amenities/**",
            "/api/room-type/**",
    };

    public static final String[] RESOURCE_URLS = {
            "/css/**",
            "/images/**"
    };

    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
}
