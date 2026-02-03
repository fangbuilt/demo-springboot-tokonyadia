package com.fangbuilt.demo_springboot_tokonyadia.member.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

/**
 * Response DTO untuk Member.
 * Password TIDAK disertakan di response (security best practice).
 */
@Builder
public record MemberRes(
    UUID id,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
