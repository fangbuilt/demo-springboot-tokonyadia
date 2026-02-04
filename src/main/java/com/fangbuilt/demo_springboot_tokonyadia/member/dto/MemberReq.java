package com.fangbuilt.demo_springboot_tokonyadia.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberReq(
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Username harus 3-100 karakter")
    String username,

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    String password) {
}
