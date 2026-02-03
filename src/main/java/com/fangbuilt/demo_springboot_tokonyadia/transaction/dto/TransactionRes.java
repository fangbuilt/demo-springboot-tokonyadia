package com.fangbuilt.demo_springboot_tokonyadia.transaction.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemRes;

import lombok.Builder;

/**
 * Response DTO untuk Transaction.
 * Include detail receipts biar frontend langsung dapet semua info.
 */
@Builder
public record TransactionRes(
    UUID id,
    LocalDateTime timestamp, // Dari createdAt
    UUID customerId,
    String customerName, // Biar frontend gak perlu fetch lagi
    List<ReceiptItemRes> items,
    Double totalAmount, // Total harga keseluruhan
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
