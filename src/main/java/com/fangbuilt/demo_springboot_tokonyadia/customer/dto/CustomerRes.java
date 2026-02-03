package com.fangbuilt.demo_springboot_tokonyadia.customer.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

import lombok.Builder;

/**
 * Response DTO untuk Customer.
 * Include timestamps biar frontend bisa display "joined date" etc.
 */
@Builder
public record CustomerRes(
    UUID id,
    String fullname,
    String email,
    String address,
    Gender gender,
    UUID memberId, // Bisa null kalau customer gak punya member account (guest)
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
