package com.fangbuilt.demo_springboot_tokonyadia.dto.response;

import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.utility.Gender;

import lombok.Builder;

@Builder
public record CustomerResponse(
    UUID id,
    String fullname,
    String email,
    String address,
    Gender gender,
    UUID memberId) {
}
