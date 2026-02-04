package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Login Request DTO
 */
public record LoginReq(
    @NotBlank(message = "Username tidak boleh kosong")
    String username,
    
    @NotBlank(message = "Password tidak boleh kosong")
    String password
) {}
