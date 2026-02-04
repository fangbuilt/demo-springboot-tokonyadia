package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import lombok.Builder;

@Builder
public record AuthRes(
    String accessToken,
    String refreshToken,
    String tokenType,
    String username,
    String role
) {
    public AuthRes(String accessToken, String refreshToken, String tokenType, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.username = username;
        this.role = role;
    }
}
