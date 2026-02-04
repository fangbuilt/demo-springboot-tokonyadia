package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import lombok.Builder;

/**
 * Authentication Response DTO
 * Ini yang di-return setelah login atau refresh token berhasil
 */
@Builder
public record AuthRes(
    String accessToken,
    String refreshToken,
    String tokenType, // Always "Bearer"
    String username,
    String role
) {
    // Default tokenType to "Bearer" kalau ga di-set
    public AuthRes(String accessToken, String refreshToken, String tokenType, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.username = username;
        this.role = role;
    }
}
