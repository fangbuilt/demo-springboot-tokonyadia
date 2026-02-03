package com.fangbuilt.demo_springboot_tokonyadia.customer.dto;

import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO untuk create/update Customer.
 * Member ID optional karena customer bisa checkout sebagai guest (no account).
 */
public record CustomerReq(
    @NotBlank(message = "Fullname tidak boleh kosong")
    @Size(max = 200, message = "Fullname maksimal 200 karakter")
    String fullname,

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Size(max = 150, message = "Email maksimal 150 karakter")
    String email,

    @Size(max = 500, message = "Address maksimal 500 karakter")
    String address,

    Gender gender,

    UUID memberId // Optional - bisa null untuk guest checkout
) {}
