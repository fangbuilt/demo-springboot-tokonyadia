package com.fangbuilt.demo_springboot_tokonyadia.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record TransactionResponse(
    UUID id,
    LocalDateTime timestamp,
    UUID customerId) {

}
