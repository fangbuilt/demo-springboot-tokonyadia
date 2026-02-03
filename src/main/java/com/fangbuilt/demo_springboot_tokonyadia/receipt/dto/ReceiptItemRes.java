package com.fangbuilt.demo_springboot_tokonyadia.receipt.dto;

import java.util.UUID;

import lombok.Builder;

/**
 * Response DTO untuk satu item receipt dalam transaction.
 * Include product info biar frontend gak perlu fetch terpisah.
 */
@Builder
public record ReceiptItemRes(
        UUID id,
        UUID productId,
        String productName, // Denormalized buat kemudahan frontend
        Double cogs, // Snapshot harga saat pembelian
        Integer quantity,
        Double subtotal // cogs * quantity
) {
}
