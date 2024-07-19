package com.project.house.rental.constant;

public class SecurityConstant {

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";

    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds

    public static final String COMPANY = "HOUSE RENTAL";

    public static final String APPLICATION_NAME = "House Rental API";

    public static final String JWT_AUTHORITIES = "authorities";

    public static final String[] API_PUBLIC_URLS = {
            "/api/login",
            "/api/user/register",
            "/api/user/resetPassword/**",
            "/api/user/image/**",
    };

    public static final String[] API_PUBLIC_GET_URLS = {
            "/api/products/**",
            "/api/file/image/**"
    };

    public static final String[] RESOURCE_URLS = {
            "/css/**",
            "/images/**"
    };

    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
}
