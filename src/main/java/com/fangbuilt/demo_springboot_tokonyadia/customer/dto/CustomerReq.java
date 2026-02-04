package com.fangbuilt.demo_springboot_tokonyadia.customer.dto;

import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    UUID memberId
) {}
