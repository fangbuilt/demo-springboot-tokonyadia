package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Refresh Token Request DTO
 * Dipakai pas user mau refresh access token tanpa login lagi
 */
public record RefreshReq(
    @NotBlank(message = "Refresh token tidak boleh kosong")
    String refreshToken
) {}
