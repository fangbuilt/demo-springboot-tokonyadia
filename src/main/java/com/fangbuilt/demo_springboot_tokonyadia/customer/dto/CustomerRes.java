package com.fangbuilt.demo_springboot_tokonyadia.customer.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

import lombok.Builder;

@Builder
public record CustomerRes(
    UUID id,
    String fullname,
    String email,
    String address,
    Gender gender,
    UUID memberId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
