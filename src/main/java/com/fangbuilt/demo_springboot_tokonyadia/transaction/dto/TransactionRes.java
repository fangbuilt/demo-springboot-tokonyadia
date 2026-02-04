package com.fangbuilt.demo_springboot_tokonyadia.transaction.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemRes;

import lombok.Builder;

@Builder
public record TransactionRes(
    UUID id,
    LocalDateTime timestamp, // Dari createdAt
    UUID customerId,
    String customerName, // Biar frontend gak perlu fetch lagi (dan snapshot)
    List<ReceiptItemRes> items,
    Double totalAmount, // Sum
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
