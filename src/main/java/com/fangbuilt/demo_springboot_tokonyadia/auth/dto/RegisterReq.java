package com.fangbuilt.demo_springboot_tokonyadia.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterReq(
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Username harus antara 3-100 karakter")
    String username,

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    String password) {
}
