package com.fangbuilt.demo_springboot_tokonyadia.receipt.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record ReceiptItemRes(
    UUID id,
    UUID productId,
    String productName, // Denormalized buat kemudahan frontend (snapshot juga)
    Double cogs, // Snapshot harga saat pembelian
    Integer quantity,
    Double subtotal // cogs * quantity
) {
}
