package com.fangbuilt.demo_springboot_tokonyadia.product.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductRes(
    UUID id,
    String name,
    Double cogm,
    Integer stock,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
