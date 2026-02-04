package com.fangbuilt.demo_springboot_tokonyadia.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * JWT Configuration - semua setting JWT ada di sini
 * Ganti secret key di application.properties buat production!
 */
@Configuration
@Getter
public class JwtConfig {
    @Value("${jwt.secret:your-super-secret-key-change-this-in-production-min-256-bits}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // Default 24 jam (dalam milliseconds)
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800000}") // Default 7 hari
    private Long refreshExpiration;
}
