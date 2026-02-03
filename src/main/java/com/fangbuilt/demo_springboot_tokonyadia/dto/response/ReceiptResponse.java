package com.fangbuilt.demo_springboot_tokonyadia.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record ReceiptResponse(
    UUID id,
    Double cogs,
    Integer quantity,
    UUID productId,
    UUID transactionId) {

}
