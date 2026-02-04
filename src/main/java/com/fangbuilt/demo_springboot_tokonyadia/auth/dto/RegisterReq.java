package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Register Request DTO
 * 
 * Cuma username & password karena:
 * - fullName, email, address, gender itu ada di Customer (business profile)
 * - Member itu pure authentication entity
 * 
 * Flow register:
 * 1. User register → create Member (username, password, role)
 * 2. User isi profile → create Customer linked ke Member
 */
public record RegisterReq(
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Username harus antara 3-100 karakter")
    String username,
    
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    String password
) {}
