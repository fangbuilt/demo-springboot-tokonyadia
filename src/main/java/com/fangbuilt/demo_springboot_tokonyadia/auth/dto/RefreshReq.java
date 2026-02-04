package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshReq(
    @NotBlank(message = "Refresh token tidak boleh kosong")
    String refreshToken
) {}
